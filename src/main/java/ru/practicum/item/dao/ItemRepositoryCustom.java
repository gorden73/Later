package ru.practicum.item.dao;

import ru.practicum.item.model.ItemInfoWithUrlState;

import java.net.URISyntaxException;
import java.util.Collection;

public interface ItemRepositoryCustom {

    Collection<ItemInfoWithUrlState> checkItemsUrls(long userId) throws URISyntaxException;
}
