package com.example.affiliates.User.Entity;

import com.example.affiliates.Util.BaseEntity;
import com.example.affiliates.Util.LoginStatus;
import com.example.affiliates.Util.Role;
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
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false)
    private String userNum;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 200)
    private String nickName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginStatus loginStatus;

    @Column(columnDefinition = "varchar(10) default 'active'")
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public UserEntity(String userNum, String password, LoginStatus loginStatus, String status, Role role,
                      String nickName){
        this.userNum = userNum;
        this.password = password;
        this.loginStatus = loginStatus;
        this.status = status;
        this.role = role;
        this.nickName = nickName;
    }
}
