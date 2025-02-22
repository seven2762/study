package com.example.concurrenttest.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order2 order;

    public OrderItem(String itemName) {
        this.itemName = itemName;
    }

    public void addOrderItem(Order2 order) {
        this.order = order;
        order.getOrderItems().add(this);
    }

//    protected void linkToOrder(Order2 order) {
//        this.order = order;
//    }

    public static OrderItem createOrderItem(String itemName) {
        OrderItem orderItem = new OrderItem();
        orderItem.itemName = itemName;

        return orderItem;
    }
}
