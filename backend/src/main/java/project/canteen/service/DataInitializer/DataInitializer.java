package project.canteen.service.DataInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import project.canteen.common.constant;
import project.canteen.entity.auth.account;
import project.canteen.entity.auth.role;
import project.canteen.repository.auth.accountRepository;
import project.canteen.repository.auth.roleRepository;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private roleRepository roleRepository;
    @Autowired
    private accountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public String generateToken() {
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            token.append(random.nextInt(10));
        }
        return token.toString();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(roleRepository.findByName(constant.ROLE.ADMIN) == null) {
            role role = new role();
            role.setName(constant.ROLE.ADMIN);
            roleRepository.saveAndFlush(role);
        }
        if(roleRepository.findByName(constant.ROLE.STAFF) == null) {
            role role = new role();
            role.setName(constant.ROLE.STAFF);
            roleRepository.saveAndFlush(role);
        }
        if(roleRepository.findByName(constant.ROLE.USER) == null) {
            role role = new role();
            role.setName(constant.ROLE.USER);
            roleRepository.saveAndFlush(role);
        }
        if(accountRepository.findUserByUsername("admin") == null) {
            account account = new account();
            account.setStatus(constant.STATUS.ACTIVE);
            account.setUsername("admin");
            account.setPassword(passwordEncoder.encode("admin"));
            account.setPhoneNumber("0123456789");
            account.setFullname("ADMIN");
            Set<role> roles = new HashSet<>();
            role role = roleRepository.findByName(constant.ROLE.ADMIN);
            if(role!= null) {
                roles.add(role);
            }
            account.setRoles(roles);
            account.setEmail("canteen@gmail.com");
            String token =generateToken();
            while (accountRepository.existsByCode(token)) {
                token = generateToken();
            }
            account.setCode(token);
            accountRepository.save(account);
        }
    }
}
