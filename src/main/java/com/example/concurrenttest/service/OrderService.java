package com.example.concurrenttest.service;

import com.example.concurrenttest.domain.Order2;
import com.example.concurrenttest.domain.OrderItem;
import com.example.concurrenttest.repository.OrderItemRepository;
import com.example.concurrenttest.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public Order2 loadOrderId(Long id) {
        return orderRepository.findByOrderId(id).orElseThrow(() -> new RuntimeException(
            "Order is not exist"));
    }
//    @Transactional
//    public Long createOrder(List<CreateOrderItemPostReq> req) {
//
//        List<OrderItem> orderItems = req.stream()
//            .map(request -> OrderItem.createOrderItem(
//                request.getItemName()
//            )).toList();
//
//        Order2 order = Order2.createOrder(orderItems);
//
//        orderRepository.save(order);
//        return order.getOrderId();
//    }

    @Transactional
    public Order2 save(String itemName) {

        Order2 newOrder = new Order2(itemName);

        return orderRepository.save(newOrder);
    }

    @Transactional
    public Order2 orderAsLazyLoading(Long orderId, String item) {

        Order2 findOrder = loadOrderId(orderId);
        OrderItem orderItem = new OrderItem(item);

        orderItem.addOrderItem(findOrder);
        orderItemRepository.save(orderItem);

        return findOrder;
    }
}
