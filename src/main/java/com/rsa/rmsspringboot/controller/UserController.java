package com.rsa.rmsspringboot.controller;

import com.rsa.rmsspringboot.entity.Role;
import com.rsa.rmsspringboot.entity.User;
import com.rsa.rmsspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public String registration(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "user/user-form";
    }

    @PostMapping("/register")
    public String registration(@ModelAttribute("user") User user, BindingResult bindingResult) {
//        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "user/user-form";
        }

        userService.save(user);

        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user, BindingResult bindingResult) {
//        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "user/user-details";
        }

        userService.save(user);

        return "redirect:/profile";
    }

    @RequestMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetail.getUsername());
        List<Role> roles = new ArrayList<>();
        roles.addAll(user.getRoles());
        for (Role role : roles) {
            if (role.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
                user.setRoleAdmin(true);
            } else {
                user.setRoleUser(true);
            }
        }

        model.addAttribute("user", user);
        return "user/user-details";
    }

    @RequestMapping("/profileByEmail")
    public String profileByIEmail(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);
        List<Role> roles = new ArrayList<>();
        roles.addAll(user.getRoles());
        for (Role role : roles) {
            if (role.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
                user.setRoleAdmin(true);
            } else {
                user.setRoleUser(true);
            }
        }

        model.addAttribute("user", user);
        return "user/user-details";
    }

    @RequestMapping("/list")
    public String listUser(Model model) {

        // get employees from db
        List<User> users = userService.findAll();
        String desc = "";
        int roleSize = 0;
        List<Role> roles = new ArrayList<>();
        for (User user : users) {
            desc = "";
            roles.addAll(user.getRoles());
            roleSize = user.getRoles().size();
            for (int i = 0; i < roleSize; i++) {
                if (roles.get(i).getRole().equalsIgnoreCase("ROLE_ADMIN")) {
                    desc += "ADMIN";
                } else {
                    desc += "USER";
                }
                if (roleSize > 1 && i < roleSize -1) {
                    desc += ", ";
                }
            }
            user.setRoleDescription(desc);
        }

        // add to the spring model
        model.addAttribute("users", users);

        return "user/list-user";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("userId") Long id) {
        userService.deleteById(id);
        return "redirect:/list";
    }

    @RequestMapping("/resetPassword")
    public String reset(@RequestParam("email") String email) {
        userService.resetPassword(email);
        return "redirect:/list";
    }
}
