package com.fundfun.fundfund.domain.payment;

import com.fundfun.fundfund.domain.order.Orders;
import com.fundfun.fundfund.domain.user.Users;
import com.fundfun.fundfund.util.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id")
    private UUID id;

    @ManyToOne
    private Users paid_by;

    @OneToOne
    private Orders order;

    @OneToOne
    private PayMean mean;

    private Long cost;

    public static Payment createPayment(Users user, Orders order, PayMean mean, Long cost) {
        Payment payment = new Payment(null, user, order, mean, cost);
        user.addPayment(payment);
        return payment;
    }
}