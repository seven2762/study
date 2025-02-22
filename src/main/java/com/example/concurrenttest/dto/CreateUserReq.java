package com.example.concurrenttest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUserReq {

    private String name;

    public static CreateUserReq create(String name) {
        CreateUserReq createUserReq = new CreateUserReq();
        createUserReq.name = name;
        return createUserReq;
    }

}
