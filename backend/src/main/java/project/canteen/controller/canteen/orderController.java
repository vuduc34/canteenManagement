package project.canteen.controller.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.canteen.common.constant;
import project.canteen.entity.canteen.foodItem;
import project.canteen.model.auth.ResponMessage;
import project.canteen.model.canteen.createOrderModel;
import project.canteen.service.canteen.orderService;

@RestController
@RequestMapping(constant.API.PREFIX)
public class orderController {
    @Autowired
    private orderService orderService;

    @PostMapping(value = "/order/create")
    @ResponseBody
    public ResponMessage createOrder(@RequestBody createOrderModel createOrderModel) {
        return orderService.createOrder(createOrderModel);
    }
    @PutMapping(value = "/order/cancel")
    @ResponseBody
    public ResponMessage cancelOrder(@RequestParam Long orderId,@RequestHeader("Authorization") String token) {
        return orderService.cancelOrder(orderId,token);
    }
    @PutMapping(value = "/order/preparing")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage preparingOrder(@RequestParam Long orderId,@RequestHeader("Authorization") String token) {
        return orderService.preparingOrder(orderId,token);
    }

    @PutMapping(value = "/order/rejected")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage rejectedOrder(@RequestParam Long orderId,@RequestHeader("Authorization") String token) {
        return orderService.rejectOrder(orderId,token);
    }

    @PutMapping(value = "/order/done")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage doneOrder(@RequestParam Long orderId,@RequestHeader("Authorization") String token) {
        return orderService.doneOrder(orderId,token);
    }

    @GetMapping(value = "/order/findOrderByAccountId")
    @ResponseBody
    public ResponMessage findOrderByAccountId(@RequestParam Long accountId) {
        return orderService.getOrderByAccountId(accountId);
    }

    @GetMapping(value = "/order/getAllOrder")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage getAllOrder() {
        return orderService.getAllOrder();
    }

    @GetMapping(value = "/order/getOrderPreparingOrUnconfirmed")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage getOrderPreparingOrUnconfirmed() {
        return orderService.getOrderPreparingOrUnconfirmed();
    }

    @GetMapping(value = "/order/getTotalRevenueLastDays")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage getTotalRevenueLastDays(@RequestParam int day) {
        return orderService.getTotalRevenueLastDays(day);
    }

    @GetMapping(value = "/order/getTotalRevenueLastMonths")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage getTotalRevenueLastMonths(@RequestParam int month) {
        return orderService.getTotalRevenueLastMonths(month);
    }

    @GetMapping(value = "/order/getTotalRevenueToday")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage getTotalRevenueToday() {
        return orderService.getTotalRevenueToday();
    }

    @GetMapping(value = "/order/getTotalDoneOrdersToday")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage getTotalDoneOrdersToday() {
        return orderService.getTotalOrdersTodayByStatus(constant.STATUS.DONE);
    }

    @GetMapping(value = "/order/getTotalPreparingOrdersToday")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage getTotalPreparingOrdersToday() {
        return orderService.getTotalOrdersTodayByStatus(constant.STATUS.PREPARING);
    }

    @GetMapping(value = "/order/getTotalUnConfirmOrdersToday")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage getTotalUnConfirmOrdersToday() {
        return orderService.getTotalOrdersTodayByStatus(constant.STATUS.UN_CONFIRMED);
    }
}
