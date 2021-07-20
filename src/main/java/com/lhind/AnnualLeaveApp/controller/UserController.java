package com.lhind.AnnualLeaveApp.controller;

import com.lhind.AnnualLeaveApp.model.User;
import com.lhind.AnnualLeaveApp.security.ApplicationRoles;
import com.lhind.AnnualLeaveApp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@Controller
@RequestMapping(path = "/api")
public class UserController {

    private final UserService userService;

    @RequestMapping(path = "/admin/home")
    public String adminhomeView(){
        return "admin/adminHome";
    }

    @RequestMapping(path = "/user/home")
    public String userHomeView(){
        return "user/userHome";
    }

    @RequestMapping(path = "/supervisor/home")
    public String supervisorHomeView(){
        return "supervisor/supervisorHome";
    }

    @GetMapping("/admin/manage-users")
    public String getAllUsers(Model model){
        model.addAttribute("userList", userService.getUsers());
        return "admin/manageUsers";
    }

    @RequestMapping("/change-password")
    public String changePassword(){
        return "changePassword";
    }

    @PostMapping("/change-password/save")
    public String savePassword(@RequestParam("currentPassword") String currentPassword,
                               @Valid @RequestParam("newPassword") String newPassword){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userService.loadUserByUsername(username);
        String encodedPassword = user.getPassword();
        if (!bCryptPasswordEncoder.matches(currentPassword,encodedPassword)){
            return "errorInvalidPassword";
        }
        user.setPassword(newPassword);
        userService.save(user);
        if (user.getRole().equals(ApplicationRoles.USER))
            return "user/userHome";
        else if (user.getRole().equals(ApplicationRoles.SUPERVISOR))
            return "supervisor/supervisorHome";
        return "admin/adminHome";
    }
    @GetMapping("admin/manage-users/delete/{id}")
    public String manageUsers(@PathVariable("id") Integer id){

            userService.delete(id);

        return "admin/manageUsers";
    }

    @GetMapping("admin/manage-users/edit/{id}")
    public String editUserForm (@PathVariable("id") Integer id, Model model){
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "admin/editUserForm";
    }

    @RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
    public String loginView(){
        return "login";
    }

    @RequestMapping(path = "/login-error")
    public String loginError(){
        return "login-error";
    }

    @RequestMapping(path = "/access-denied")
    public String accessDenied(){
        return "accessDenied";
    }

    @GetMapping("/admin/add-user")
    public String newUser(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "admin/newUserForm";
    }
    @PostMapping("/admin/save-user")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult){
         if (bindingResult.hasErrors()){
            return "admin/saveUserError";
        }
         userService.save(user);
        return "admin/saveUserSuccess";
    }

}
