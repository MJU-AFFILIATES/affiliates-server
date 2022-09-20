package com.example.affiliates.Store.Service;

import com.example.affiliates.Store.Repository.StoreRepository;
import com.example.affiliates.User.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
    private StoreRepository storeRepository;
    public StoreService(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }
}
