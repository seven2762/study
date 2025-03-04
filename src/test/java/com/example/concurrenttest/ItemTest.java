package com.example.concurrenttest;

import com.example.concurrenttest.service.ItemService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.IntStream;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
public class ItemTest {

    @Autowired
    ItemService itemService;

    Logger logger = Logger.getLogger(ItemTest.class.getName());

    @Test
    @DisplayName("아이템 생성")
    @Rollback(false)
    @Transactional
    void createItem() {
        itemService.save("칫솔", 100);
    }

    @Test
    @DisplayName("재고 감소 동시성 테스트")
    @Rollback(false)
    @Transactional
    void updateItem() throws InterruptedException {
        AtomicInteger successAtomicInteger = new AtomicInteger(0);
        AtomicInteger failedAtomicInteger1 = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch countDownLatch = new CountDownLatch(30);
        IntStream.range(0, 30).forEach(i -> executorService.execute(() -> {
            try {
                itemService.updateItem(6L, 1);
                successAtomicInteger.incrementAndGet();
            } catch (Exception e) {
                e.printStackTrace();
                failedAtomicInteger1.incrementAndGet();
            } finally {
                countDownLatch.countDown();
            }
        }));
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("성공 : " + successAtomicInteger.get() + "실패 : " + failedAtomicInteger1.get());
    }
    @Test
    @DisplayName("데드락 테스트")
    @Transactional
    void deadlockTest() throws InterruptedException {
        // 테스트용 아이템 2개 생성
        itemService.save("상품1", 100);  // id: 1
        itemService.save("상품2", 100);  // id: 2
        int numberOfThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        try {
            IntStream.range(0, numberOfThreads).forEach(i -> {
                executorService.execute(() -> {
                    try {
                        if (i == 0) {
                            itemService.deadlockScenario(1L, 2L);
                        } else {
                            itemService.deadlockScenario(2L, 1L);
                        }
                    } catch (Exception e) {
                        log.error("Thread {} error: ", i, e);
                    } finally {
                        latch.countDown();
                    }
                });
            });
            latch.await(10, TimeUnit.SECONDS);
        } finally {
            executorService.shutdown();
        }
        }
}
