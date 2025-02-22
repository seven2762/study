package com.example.concurrenttest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order2")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    private String itemName;

    //    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order2(String itemName) {
        this.itemName = itemName;
    }

//    public void addOrderItem(OrderItem orderItem) {
//        orderItems.add(orderItem);
////        orderItem.linkToOrder(this);
//    }

//    public static Order2 createOrder(List<OrderItem> orderItems) {
//        Order2 order = new Order2();
//        orderItems.forEach(order::addOrderItem);
//
//        return order;
//    }


}
