package project.canteen.controller.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.canteen.common.constant;
import project.canteen.model.auth.ResponMessage;
import project.canteen.service.canteen.cartService;

@RestController
@RequestMapping(constant.API.PREFIX)
public class cartController {
    @Autowired
    private cartService cartService;

    @GetMapping(value = "/cart/findByCartId")
    @ResponseBody
    public ResponMessage addFoodToCart(@RequestParam Long cartId) {
        return cartService.findByCartId(cartId);
    }

}
