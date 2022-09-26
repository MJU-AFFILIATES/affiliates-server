package com.example.affiliates.Store.Entity;

import com.example.affiliates.Util.BaseEntity;
import com.example.affiliates.Util.CategoryEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "store")
@NoArgsConstructor
@DynamicInsert
public class StoreEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeIdx;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryEnum categoryEnum;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 200)
    private String contents;

    @Column(nullable = false, columnDefinition = "varchar(10) default 'active'")
    private String status;

    @Builder
    public StoreEntity(String name, CategoryEnum categoryEnum,
                       String address, String contents, String status){
        this.name = name;
        this.categoryEnum = categoryEnum;
        this.address = address;
        this.contents = contents;
        this.status = status;
    }

}
