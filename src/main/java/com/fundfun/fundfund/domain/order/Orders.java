package com.fundfun.fundfund.domain.order;

import com.fundfun.fundfund.domain.payment.Payment;
import com.fundfun.fundfund.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @Column(name = "orders_id")
    private UUID id;

    private int cost;
    private String orderDate;
    private String status;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private UUID userId;

}