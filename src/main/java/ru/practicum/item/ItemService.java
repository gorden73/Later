package ru.practicum.item;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ItemService {

    List<ItemDto> getItems(long userId);

    List<ItemDto> getItems(GetItemRequest req);

    ItemDto update(ModifyItemRequest req) throws IllegalAccessException;

    ItemDto addNewItem(long userId, ItemDto item);

    void deleteItem(long userId, long itemId) throws IllegalAccessException;

    Collection<ItemInfoWithUrlState> checkItemsUrls(long userId) throws URISyntaxException;
}
