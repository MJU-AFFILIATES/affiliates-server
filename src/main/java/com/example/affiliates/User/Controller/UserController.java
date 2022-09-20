package com.example.affiliates.User.Controller;

import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public void Login(@RequestBody UserDTO.Login user){
        this.userService.login(user);
    }
}
