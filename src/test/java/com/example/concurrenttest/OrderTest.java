package com.example.concurrenttest;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.concurrenttest.domain.Order2;
import com.example.concurrenttest.domain.OrderItem;
import com.example.concurrenttest.repository.OrderRepository;
import com.example.concurrenttest.service.OrderService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;
//    @Test
//    @Rollback(value = false)
//    @Transactional
//    @DisplayName("주문과 주문 아이템이 정상적으로 저장된다.")
//    void createOrder() {
//        List<CreateOrderItemPostReq> requests = List.of(
//            new CreateOrderItemPostReq("item1"),
//            new CreateOrderItemPostReq("item2")
//        );
//
//        Long orderId = orderService.createOrder(requests);
////        Order2 order = orderRepository.findById(orderId).orElseThrow();
////
////
////        assertThat(order.getOrderItems()).hasSize(requests.size());
//    }
//
    @Test
    @DisplayName("페치 조인 없이 주문 id로 주문 주문 상품 조회하기")
    @Transactional(readOnly = true)
    void findOrderWithoutFetch() {
        Order2 order = orderRepository.findByOrderId(6L).orElseThrow();
        List<OrderItem> orderItems = order.getOrderItems();

        assertThat(orderItems).hasSize(2);
    }
}
