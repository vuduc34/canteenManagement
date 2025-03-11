package project.canteen.model.canteen;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class cartItemInfo {
    private Long id;
    private int quantity;
    private Long price;
    private Long foodId;
    private Long foodPrice;
    private String foodName;
    private String description;
    private String imageUrl;
    private String status;
}
