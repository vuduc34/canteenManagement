package project.canteen.model.auth;

import lombok.Getter;
import lombok.Setter;
import project.canteen.entity.auth.account;

@Getter
@Setter
public class AccountDTO {
    private Long id;
    private String username;
    private String password;
    private Long status;
    private String tokenForgotPassword;
    private String timeCreatioToken;
    private String email;
    private String code;
    private String fullname;
    private String role;
    private String phoneNumber;
    public account toObject() {
        account account = new account();
        account.setId(id);
        account.setUsername(username);
        account.setPassword(password);
        account.setStatus(status);
        account.setTokenForgotPassword(tokenForgotPassword);
        account.setEmail(email);
        account.setCode(code);
        account.setFullname(fullname);
        account.setPhoneNumber(phoneNumber);
        return  account;
    }
}
