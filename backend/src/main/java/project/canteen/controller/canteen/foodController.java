package project.canteen.controller.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.canteen.common.constant;
import project.canteen.model.auth.ResponMessage;
import project.canteen.service.canteen.categoryService;
import project.canteen.service.canteen.foodService;

@RestController
@RequestMapping(constant.API.PREFIX_AUTH)
public class foodController {
    @Autowired
    private foodService foodService;

    @GetMapping("/food/findAll")
    @ResponseBody
    public ResponMessage findAll() {
        return foodService.findAlll();
    }

    @GetMapping("/food/findAllAvailable")
    @ResponseBody
    public ResponMessage findAllAvailable() {
        return foodService.findAllAvailable();
    }

    @GetMapping("/food/findAllPageable")
    @ResponseBody
    public ResponMessage findAllPageable(
            @RequestParam(defaultValue = "0") int page,  // Trang mặc định là 0
            @RequestParam(defaultValue = "5") int size  // Số lượng mặc định là 5
    ) {
        return foodService.findAllPageable(page, size);
    }

    @GetMapping("/food/findByCategoryId")
    @ResponseBody
    public ResponMessage findByCategoryId(@RequestParam Long categoryId) {
        return foodService.findByCategory(categoryId);
    }
}
