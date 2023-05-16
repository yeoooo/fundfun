package com.fundfun.fundfund.domain.product;

import com.fundfun.fundfund.domain.order.Orders;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private UUID id;

    private String crowdStart;
    private String crowdEnd;
    private int goal;
    private int currentGoal;
    private String status;
    private String description;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Users users;

    public Product(UUID id){
        this.id = id;
    }
}