package com.example.affiliates.User.Service;

import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void login(UserDTO.Login user) {

    }
}
