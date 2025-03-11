package project.canteen.model.canteen;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class addCartItemModel {
    private Long accountId;
    private Long foodId;
    private int quantity;
    private Long cartId;
}
