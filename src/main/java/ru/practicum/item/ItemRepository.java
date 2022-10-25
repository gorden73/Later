package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom,
        QuerydslPredicateExecutor<Item> {

    Optional<Item> findItemById(long itemId);

    List<Item> findByUserId(long userId);

    List<ItemInfo> findAllByUserId(long userId);

    void deleteByUserIdAndId(long userId, long itemId);

    Item findItemByResolvedUrl(String resolvedUrl);
}