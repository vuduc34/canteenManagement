package project.canteen.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.canteen.config.jwtConfig.customUserDetail;
import project.canteen.entity.auth.account;
import project.canteen.repository.auth.accountRepository;

@Service
public class customUserDetailService implements UserDetailsService {
    @Autowired
    private accountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        account account = accountRepository.findUserByUsername(username);
        return customUserDetail.createCustomUserDetails(account);
    }
}
