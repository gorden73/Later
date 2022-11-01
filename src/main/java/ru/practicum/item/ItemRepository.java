package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom,
        QuerydslPredicateExecutor<Item> {

    Optional<Item> findItemById(long itemId);

    List<ItemInfo> findAllByUserId(long userId);

    void deleteByUserIdAndId(long userId, long itemId);

    Item findItemByUserIdAndResolvedUrl(Long userId, String resolvedUrl);
}