package com.example.concurrenttest.repository;

import com.example.concurrenttest.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
