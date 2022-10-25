package ru.practicum.item;

import java.net.URISyntaxException;
import java.util.Collection;

public interface ItemRepositoryCustom {
    Collection<ItemInfoWithUrlState> checkItemsUrls(long userId) throws URISyntaxException;
}
