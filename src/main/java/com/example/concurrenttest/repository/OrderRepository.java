package com.example.concurrenttest.repository;

import com.example.concurrenttest.domain.Order2;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order2, Long> {

    Optional<Order2> findById(Long orderId);

    @EntityGraph(attributePaths = {"orderItems"})
    Optional<Order2> findByOrderId(Long orderId);
}
