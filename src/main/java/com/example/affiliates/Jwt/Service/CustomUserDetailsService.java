package com.example.affiliates.Jwt.Service;

import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userNum) throws UsernameNotFoundException {
        return userRepository.findByUserNum(userNum)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(userNum + " -> 데이터베이스에서 찾을 수 없습니다."));

    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(UserEntity user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().toString());
        return new User(
                String.valueOf(user.getUserNum()),
                user.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
