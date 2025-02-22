package com.example.concurrenttest.repository;

import com.example.concurrenttest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String username);
}
