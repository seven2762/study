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
        Item item = itemRepository.findByIdWithPessimisticLock(id)
            .orElseThrow(() -> new RuntimeException("없는 상품 입니다"));
        item.decreaseStock(quantity);
    }

    @Transactional
    public void deadlockScenario(Long itemId1 , Long itemId2) {
        Item item = itemRepository.findByIdWithPessimisticLock(itemId1).orElseThrow();
        item.decreaseStock(1);
        try {
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        }
        Item item2 = itemRepository.findByIdWithPessimisticLock(itemId2).orElseThrow();
        item2.decreaseStock(1);
    }

}


