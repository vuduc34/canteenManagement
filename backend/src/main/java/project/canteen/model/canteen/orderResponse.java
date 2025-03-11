package project.canteen.model.canteen;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class orderResponse {
    private Long id;
    private Long totalPrice;
    private String status;
    private String code;
    private String orderTime;
}
