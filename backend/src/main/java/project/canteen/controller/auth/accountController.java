package project.canteen.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.canteen.common.constant;
import project.canteen.model.auth.ResponMessage;
import project.canteen.model.auth.signUpData;
import project.canteen.service.auth.accountService;

@RestController
@RequestMapping(constant.API.PREFIX)
public class accountController {
    @Autowired
    private accountService accountService;

    @PostMapping("/account/create")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponMessage createAccount(@RequestBody signUpData signUpData,@RequestParam String roleName) throws Exception {
        return accountService.createAccount(signUpData,roleName);
    }

    @GetMapping("/account/changeRole")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponMessage changeRole(@RequestParam String username, @RequestParam String role) {
        return accountService.changeRole(username, role);
    }
    @GetMapping("/account/findAll")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage findAlll(@RequestParam String role) {
        return accountService.findAll(role);
    }
    @GetMapping("/account/findAllRole")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage findAlllRole() {
        return accountService.findAllRole();
    }

    @GetMapping("/account/activeAccount")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage activeAccount(@RequestParam String username) {
        return accountService.activeStatus(username);
    }

    @GetMapping("/account/deactiveAccount")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponMessage deactiveAccount(@RequestParam String username) {
        return accountService.deactiveStatus(username);
    }
    @DeleteMapping("/account/delete")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponMessage delete(@RequestParam String username) {
        return accountService.deleteAccount(username);
    }

}
