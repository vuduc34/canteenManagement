package project.canteen.config.jwtConfig;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.canteen.entity.auth.account;
import project.canteen.entity.auth.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class customUserDetail implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> roles;

    public static customUserDetail createCustomUserDetails(account account) {
        if (account != null) {
            List<GrantedAuthority> roleAuthorities = new ArrayList<>();
            for (role role : account.getRoles()) {
                roleAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }

            customUserDetail customUserDetails = new customUserDetail();
            customUserDetails.setUsername(account.getUsername());
            customUserDetails.setPassword(account.getPassword());
            customUserDetails.setRoles(roleAuthorities);
            return customUserDetails;
        }
        return null;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return roles;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return password;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
