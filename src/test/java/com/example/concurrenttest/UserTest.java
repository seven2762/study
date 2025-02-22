package com.example.concurrenttest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.concurrenttest.dto.CreateUserReq;
import com.example.concurrenttest.dto.UserRes;
import com.example.concurrenttest.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StopWatch;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserService userService;
    @Autowired
    MockMvc mockMvc;


    @Test
    @DisplayName("유저 생성 테스트")
    void createUserTest() {

        CreateUserReq createUserReq = CreateUserReq.create("lee");
        userService.createUser(createUserReq);
    }

    @Test
    @DisplayName("100개 동시 회원가입 요청시 id값에 대한 동시성 문제가 발생하는지 확인한다")
    void concurrentTestToCreatUser() throws InterruptedException {

        //given
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //when
        IntStream.range(0, numberOfThreads)
            .forEach(i -> executorService.execute(() -> {
                try {
                    CreateUserReq createUserReq = CreateUserReq.create("park");
                    userService.createUserWithSleep(createUserReq);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            }));
        countDownLatch.await();
        stopWatch.stop();

        System.out.println("총 실행 시간 : " + stopWatch.getTotalTimeMillis() + "ms");
        System.out.println("성공 횟수 :" + successCount.get() + "/ 실패 횟수 : " + failedCount.get());
        executorService.shutdown();
    }
    @Test
    @DisplayName("Mock 유저 생성 테스트")
    void createUserTestWithMock() throws Exception {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //given
        CreateUserReq createUserReq = CreateUserReq.create("lee");
        String content = new ObjectMapper().writeValueAsString(createUserReq);

        //when
        MvcResult result = mockMvc.perform(post("/api/users/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isCreated())
            .andReturn();

        stopWatch.stop();

        System.out.println("실행시간 : " + stopWatch.getTotalTimeMillis() + "ms");

        //then
        String responseBody = result.getResponse().getContentAsString();
        UserRes res = new ObjectMapper().readValue(responseBody, UserRes.class);
        System.out.println("res : " + res);
        assertThat(res.name()).isEqualTo("lee");
        assertThat(res.id()).isNotNull();
    }


}
