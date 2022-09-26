package com.example.affiliates.Store.Controller;

import com.example.affiliates.Store.DTO.StoreDTO;
import com.example.affiliates.Store.Service.StoreService;
import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Service.UserService;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/stores")
public class StoreController{
    private StoreService storeService;

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

}
