package com.example.concurrenttest.service;

import com.example.concurrenttest.domain.User;
import com.example.concurrenttest.dto.CreateUserReq;
import com.example.concurrenttest.dto.UserRes;
import com.example.concurrenttest.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRes createUser(CreateUserReq req) {
        User user = User.createUser(req.getName());
        User saveUser = userRepository.save(user);
        return new UserRes(saveUser.getId(), saveUser.getName());
    }
    public void createUserWithSleep(CreateUserReq req) {
        try {
            Thread.sleep(1000);  // Race Condition 유도

            User user = User.createUser(req.getName());
            User savedUser = userRepository.save(user);
            new UserRes(savedUser.getId(), savedUser.getName());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", e);
        }
    }
//    public UserRes createUserWithSleep(CreateUserReq req) {
//        if (!userRepository.existsByName(req.getName())) {
//            try {
//                Thread.sleep(1000);  // Race Condition 유도
//
//                User user = User.createUser(req.getName());
//                User savedUser = userRepository.save(user);
//                return new UserRes(savedUser.getId(), savedUser.getName());
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();  // 인터럽트 상태 복구
//                throw new RuntimeException("Thread was interrupted", e);
//            }
//        }
//        throw new IllegalArgumentException("이미 존재하는 이름입니다.");
//    }


    public void createUserIncludeName(String name) {
        User user = new User();
        user.setName(name);
        userRepository.save(user);
    }
}
