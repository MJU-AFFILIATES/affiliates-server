package com.example.affiliates.Store.Controller;

import com.example.affiliates.Store.Service.StoreService;
import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/stores")
public class StoreController{
    private StoreService storeService;

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

}
