package project.canteen.model.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class loginResponse {
	private String token;
	private String username;
	private String role;
	private Long account_id;
	private Long cart_id;
	private String fullname;
	private Long status;
	private String email;
	private String phoneNumber;
}
