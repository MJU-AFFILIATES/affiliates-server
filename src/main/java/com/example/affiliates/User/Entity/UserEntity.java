package com.example.affiliates.User.Entity;

import com.example.affiliates.Util.LoginStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
@DynamicInsert
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false)
    private String userNum;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginStatus loginStatus;

    @Column(columnDefinition = "varchar(10) default 'active'")
    private String status;

    @Builder
    public UserEntity(String userNum, String password, LoginStatus loginStatus, String status){
        this.userNum = userNum;
        this.password = password;
        this.loginStatus = loginStatus;
        this.status = status;
    }
}
