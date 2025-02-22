package com.example.concurrenttest;

import com.example.concurrenttest.domain.Order2;
import com.example.concurrenttest.domain.OrderItem;
import com.example.concurrenttest.repository.OrderRepository;
import com.example.concurrenttest.service.OrderService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class OrderItemTest {

    @Autowired
    OrderRepository orderRepository;

    Order2 setUpOrder2;
    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        String itemName = "item1";
        setUpOrder2 = orderService.save(itemName);
    }

    @Test
    @Rollback(value = false)
    void orderAsLazyLoadingTest() {
        Order2 setOrderItems = orderService.orderAsLazyLoading(setUpOrder2.getOrderId(),
            "sample-item");

        List<OrderItem> orderItemList = setOrderItems.getOrderItems();
        for (OrderItem orderItem : orderItemList) {
            System.out.println("OrderItem: " + orderItem);
        }
    }
}
