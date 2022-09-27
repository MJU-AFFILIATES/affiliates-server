package com.example.affiliates.Store.Entity;

import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.Util.BaseEntity;
import com.example.affiliates.Util.BaseException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "review")
public class ReviewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private UserEntity userIdx;

    @ManyToOne
    @JoinColumn(name = "storeIdx")
    private StoreEntity storeIdx;

    @Column(length = 150)
    private String review;

    @Column(nullable = false)
    private int star;

    @Builder
    public ReviewEntity(UserEntity userIdx, StoreEntity storeIdx, String review, int star){
        this.userIdx = userIdx;
        this.storeIdx = storeIdx;
        this.review = review;
        this.star = star;
    }
}
