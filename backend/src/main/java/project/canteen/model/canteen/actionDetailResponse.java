package project.canteen.model.canteen;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class actionDetailResponse {
    private String accountStaffName;
    private String actionName;
    private String accountUserName;
    private Long totalPrice;
    private String timeCreate;
}
