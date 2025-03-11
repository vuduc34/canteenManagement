package project.canteen.controller.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.canteen.common.constant;
import project.canteen.entity.canteen.foodItem;
import project.canteen.model.auth.ResponMessage;
import project.canteen.service.canteen.foodService;

@RestController
@RequestMapping(constant.API.PREFIX)
public class foodStaffController {
    @Autowired
    private foodService foodService;

    @PostMapping(value = "/food/create")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage createFood(@RequestBody foodItem foodItem, @RequestParam Long category_id) {
        return foodService.create(foodItem,category_id);
    }

    @PutMapping(value = "/food/update")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage updateFood(@RequestBody foodItem foodItem) {
        return foodService.update(foodItem);
    }

    @DeleteMapping (value = "/food/delete")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage deletFood(@RequestParam Long foodId) {
        return foodService.delete(foodId);
    }



}
