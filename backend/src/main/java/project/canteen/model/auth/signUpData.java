package project.canteen.model.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class signUpData {
    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;
    private String fullname;
}
