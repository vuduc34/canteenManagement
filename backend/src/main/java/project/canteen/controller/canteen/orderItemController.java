package project.canteen.controller.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.canteen.common.constant;
import project.canteen.model.auth.ResponMessage;
import project.canteen.service.canteen.orderService;

@RestController
@RequestMapping(constant.API.PREFIX)
public class orderItemController {
    @Autowired
    private orderService orderService;

    @GetMapping(value = "/orderItem/findOrderItemByOrderId")
    @ResponseBody
    public ResponMessage findOrderItemByOrderId(@RequestParam Long orderId) {
        return orderService.getOrderItemByOrderId(orderId);
    }
}
