package project.canteen.controller.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.canteen.common.constant;
import project.canteen.model.auth.ResponMessage;
import project.canteen.service.canteen.actionDetailService;

@RestController
@RequestMapping(constant.API.PREFIX)
public class actionDetailController {
    @Autowired
    private actionDetailService actionDetailService;


    @GetMapping(value = "/actionDetail/findByUsername")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @ResponseBody
    public ResponMessage findByUsername(@RequestParam String username) {
        return actionDetailService.findActionDetailByUserName(username);
    }
}
