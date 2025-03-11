package project.canteen.controller.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.canteen.common.constant;
import project.canteen.model.auth.ResponMessage;
import project.canteen.model.canteen.addCartItemModel;
import project.canteen.model.canteen.updateCartItemModel;
import project.canteen.service.canteen.cartItemService;

@RestController
@RequestMapping(constant.API.PREFIX)
public class cartItemController {
    @Autowired
    private cartItemService cartItemService;

    @PostMapping(value = "/cartItem/add")
    @ResponseBody
    public ResponMessage addFoodToCartItem(@RequestBody addCartItemModel addCartItemModel) {
        return cartItemService.addFoodToCart(addCartItemModel);
    }
    @PutMapping(value = "/cartItem/update")
    @ResponseBody
    public ResponMessage updateCartItem(@RequestBody updateCartItemModel updateCartItemModel) {
        return cartItemService.updateCartItem(updateCartItemModel);
    }

    @DeleteMapping(value = "/cartItem/delete")
    @ResponseBody
    public ResponMessage deleteCartItem(@RequestParam Long cartItemId) {
        return cartItemService.deleteCartItem(cartItemId);
    }
    @GetMapping(value = "/cartItem/info")
    @ResponseBody
    public ResponMessage getCartItemByCartId(@RequestParam Long cartId) {
        return cartItemService.getCartInfo(cartId);
    }

}
