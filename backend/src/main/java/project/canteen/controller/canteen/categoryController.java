package project.canteen.controller.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.canteen.common.constant;
import project.canteen.model.auth.ResponMessage;
import project.canteen.service.canteen.categoryService;

@RestController
@RequestMapping(constant.API.PREFIX_AUTH)
public class categoryController {
    @Autowired
    private categoryService categoryService;
    @GetMapping("/category/findAll")
    @ResponseBody
    public ResponMessage findAll() {
        return categoryService.findAlll();
    }
}
