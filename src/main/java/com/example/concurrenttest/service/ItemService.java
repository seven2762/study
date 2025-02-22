package com.example.concurrenttest.service;


import com.example.concurrenttest.domain.Item;
import com.example.concurrenttest.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void save(String itemName, int stock) {
        Item item = new Item(itemName, stock);

        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long id, int quantity) {

        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("없는 상품 입니다"));
        item.decreaseStock(quantity);
    }

    @Transactional
    public void deadlockScenario1(Long itemId, Long itemId2) {
        Item item1 = itemRepository.findByIdWithPessimisticLock(itemId).orElseThrow();
        item1.decreaseStock(1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Item item2 = itemRepository.findByIdWithPessimisticLock(itemId2).orElseThrow();
        item2.decreaseStock(1);
    }

    @Transactional
    public void deadlockScenario2(Long itemId1, Long itemId2) {
        Item item2 = itemRepository.findByIdWithPessimisticLock(itemId2).orElseThrow();
        item2.decreaseStock(1);

        // 의도적으로 지연 발생
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        Item item1 = itemRepository.findByIdWithPessimisticLock(itemId1).orElseThrow();
        item1.decreaseStock(1);
    }
}


