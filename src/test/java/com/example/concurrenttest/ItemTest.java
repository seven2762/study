package com.example.concurrenttest;

import com.example.concurrenttest.service.ItemService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ItemTest {

    @Autowired
    ItemService itemService;


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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateItem() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        IntStream.range(0, 3).forEach(i -> executorService.execute(() -> {
            try {
                itemService.updateItem(1L, 3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }));
        countDownLatch.await();
        executorService.shutdown();

    }

    @Test
    @DisplayName("데드락 테스트")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void deadlockTest() throws InterruptedException {
        // 테스트용 아이템 2개 생성
        itemService.save("상품1", 100);  // id: 1
        itemService.save("상품2", 100);  // id: 2

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        // 첫 번째 트랜잭션: 1번 상품 -> 2번 상품 순으로 접근
        executorService.execute(() -> {
            try {
                itemService.deadlockScenario1(1L, 2L);
            } catch (Exception e) {

            } finally {
                latch.countDown();
            }
        });

        // 두 번째 트랜잭션: 2번 상품 -> 1번 상품 순으로 접근
        executorService.execute(() -> {
            try {
                itemService.deadlockScenario2(1L, 2L);
            } catch (Exception e) {

            } finally {
                latch.countDown();
            }
        });

        latch.await();
        executorService.shutdown();
    }
}
