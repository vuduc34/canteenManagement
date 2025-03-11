package project.canteen.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import project.canteen.common.constant;
import project.canteen.config.jwtConfig.jwtProvider;
import project.canteen.entity.auth.account;
import project.canteen.entity.auth.role;
import project.canteen.entity.canteen.cart;
import project.canteen.model.auth.*;
import project.canteen.repository.auth.accountRepository;
import project.canteen.repository.auth.roleRepository;
import project.canteen.repository.canteen.cartRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class accountService {
    private static final int EXPIRE_TOKEN = 10;
    @Autowired
    private accountRepository accountRepository;
    @Autowired
    private emailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private roleRepository roleRepository;
    @Autowired
    private jwtProvider jwtProvider;
    @Autowired
    private cartRepository cartRepository;


    public String forgotPassword(String username) throws Exception {
        account account = accountRepository.findUserByUsername(username);
        if (account == null)
            return "not found in system";
        String token = generateToken();
        while (accountRepository.existsByTokenForgotPassword(token)) {
            token = generateToken();
        }
        account.setTokenForgotPassword(token);
        account.setTimeCreatioToken(Instant.now());
        accountRepository.save(account);
//		System.out.println(account.getEmail());
        try {
            String message = emailService.sendEmail(token, account.getEmail());
            return message;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean resetPassword(String token, String password) {
        if (token == null)
            return false;
        if (password == null)
            return false;
        account account = accountRepository.findByTokenForgotPassword(token);
        if (account == null)
            return false;
        if (isexpire(account.getTimeCreatioToken()))
            return false;
        String newpasswordString = passwordEncoder.encode(password);
        account.setPassword(newpasswordString);
        accountRepository.save(account);
        return true;

    }

    private Boolean isexpire(Instant timeCreation) {
        Instant now = Instant.now();
        Duration diff = Duration.between(timeCreation, now);

        return diff.toMinutes() >= EXPIRE_TOKEN;

    }

    public String generateToken() {
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            token.append(random.nextInt(10));
        }
        return token.toString();
    }

    public void sendMailActiveAccount(String code, String email) throws Exception {
        try {
             emailService.sendMailRegister(code, email);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponMessage signIn( SignInData data) {

        account account = accountRepository.findUserByUsername(data.getUserName());
        ResponMessage responMessage = new ResponMessage();
//		logger.info(jwtProvider.getClientIp());
        if (account == null) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.NOT_FOUND_USER);
            return responMessage;
        } else if (!passwordEncoder.matches(data.getPassWord(), account.getPassword())) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.PASSWORD_INCORRECT);
            return responMessage;

        } else if (account.getStatus() == constant.STATUS.DE_ACTIVE) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ACCOUNT_DEACTIVE);
            return responMessage;
        } else {
            try {
                String token = jwtProvider.generateToken(data.getUserName());
                Set<role> roles = account.getRoles();
                loginResponse loginResponse = new loginResponse();
                loginResponse.setToken(token);
                loginResponse.setUsername(data.getUserName());
                loginResponse.setRole(roles.iterator().next().getName());
                loginResponse.setAccount_id(account.getId());
                loginResponse.setFullname(account.getFullname());
                loginResponse.setEmail(account.getEmail());
                loginResponse.setStatus(account.getStatus());
                loginResponse.setPhoneNumber(account.getPhoneNumber());
                //get cart id
                cart cart = account.getCart();
                if(cart == null) {
                    cart newCart = new cart();
                    newCart.setTotalPrice(0L);
                    newCart.setAccount(account);
                    cartRepository.save(newCart);
                }
                cart = cartRepository.findCartByAccountId(account.getId());
                loginResponse.setCart_id(cart.getId());
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(loginResponse);
                return responMessage;
            } catch (Exception e) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData(e.getMessage());
                return responMessage;
            }
        }
    }

    public ResponMessage createAccount(signUpData signUp, String roleName) throws Exception {
        ResponMessage responMessage = new ResponMessage();
        try{
            role role = roleRepository.findByName(roleName);
            if (accountRepository.existsByUsername(signUp.getUserName())) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.USERNAME_EXIST);
                return responMessage;

            } else if (accountRepository.existsByEmail(signUp.getEmail())) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.EMAIL_EXIST);
                return responMessage;
            } else if(role == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ROLE_ERROR);
                return responMessage;
            } else {
                account account = new account();
                account.setStatus(constant.STATUS.DE_ACTIVE);
                account.setUsername(signUp.getUserName());
                account.setPassword(passwordEncoder.encode(signUp.getPassWord()));
                account.setPhoneNumber(signUp.getPhoneNumber());
                account.setFullname(signUp.getFullname());
                Set<role> roles = new HashSet<>();
                roles.add(role);
                account.setRoles(roles);
                account.setEmail(signUp.getEmail());
                String token =generateToken();
                while (accountRepository.existsByCode(token)) {
                    token = generateToken();
                }
                account.setCode(token);
                accountRepository.save(account);
                sendMailActiveAccount(token, signUp.getEmail());
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(account);
                return responMessage;
            }
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
            return  responMessage;
        }

    }

    public ResponMessage activeAccount(@RequestParam String code) {
        ResponMessage responMessage = new ResponMessage();
        account account = accountRepository.findByCode(code);
        if (account != null) {
            account.setStatus(constant.STATUS.ACTIVE);
            accountRepository.save(account);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
        } else {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage changeRole(String username,String role) {
        ResponMessage responMessage = new ResponMessage();
        try {
            account account = accountRepository.findUserByUsername(username);
            role roles = roleRepository.findByName(role);
            if(account == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.NOT_FOUND_USER);
            } else if(roles == null){
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ROLE_ERROR);
            }  else{
                Set<role> set = new HashSet<>();
                set.add(roles);
                account.setRoles(set);
                accountRepository.save(account);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
            }

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage activeStatus(String username) {
        ResponMessage responMessage = new ResponMessage();
        try {
            account account = accountRepository.findUserByUsername(username);
            if(account == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.NOT_FOUND_USER);
            } else {
                account.setStatus(constant.STATUS.ACTIVE);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(accountRepository.save(account).toDTO());
            }

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
        }
        return responMessage;
    }
    public ResponMessage deactiveStatus(String username) {
        ResponMessage responMessage = new ResponMessage();
        try {
            account account = accountRepository.findUserByUsername(username);
            if(account == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.NOT_FOUND_USER);
            } else {
                account.setStatus(constant.STATUS.DE_ACTIVE);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(accountRepository.save(account).toDTO());
            }

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
        }
        return responMessage;
    }


    public ResponMessage findAll(String role) {
        ResponMessage responMessage = new ResponMessage();
        try {
            List<AccountDTO> list = new ArrayList<>();
            if(role.equals(constant.ROLE.ADMIN)) {
                accountRepository.findAll().forEach(e -> {
                    AccountDTO accountDTO  = e.toDTO();
                    if(e.getRoles().size()!=0)
                        accountDTO.setRole(e.getRoles().iterator().next().getName());
                    list.add(accountDTO);
                });
            } else if(role.equals(constant.ROLE.STAFF)) {
                accountRepository.findAll().forEach(e -> {
                    if(e.getRoles().iterator().next().getName().equals(constant.ROLE.USER)) {
                        AccountDTO accountDTO  = e.toDTO();
                        if(e.getRoles().size()!=0)
                            accountDTO.setRole(e.getRoles().iterator().next().getName());
                        list.add(accountDTO);
                    }
                });
            }
            responMessage.setData(list);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage findAllRole() {
        ResponMessage responMessage = new ResponMessage();
        try {

            List<role> roles = new ArrayList<>();
            roleRepository.findAll().forEach(e -> {
                e.setAccount(null);
                roles.add(e);
            });
            responMessage.setData(roles);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
        }
        return responMessage;
    }
    public ResponMessage deleteAccount(String username) {
        ResponMessage responMessage = new ResponMessage();
        try {
            account account = accountRepository.findUserByUsername(username);
            account.setRoles(null);
            accountRepository.save(account);
            accountRepository.delete(account);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
        }
        return responMessage;
    }





}
