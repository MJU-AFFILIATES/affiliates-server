package com.example.affiliates.Store.Service;

import com.example.affiliates.Store.DTO.StoreDTO;
import com.example.affiliates.Store.Repository.StoreRepository;
import com.example.affiliates.User.Repository.UserRepository;
import com.example.affiliates.Util.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    private StoreRepository storeRepository;
    public StoreService(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

}
