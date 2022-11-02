package ru.practicum.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.ItemInfo;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom,
        QuerydslPredicateExecutor<Item> {

    Optional<Item> findItemById(long itemId);

    List<ItemInfo> findAllByUserId(long userId);

    void deleteByUserIdAndId(long userId, long itemId);

    Item findItemByUserIdAndResolvedUrl(Long userId, String resolvedUrl);
}