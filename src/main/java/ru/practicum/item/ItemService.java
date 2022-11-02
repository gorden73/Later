package ru.practicum.item;

import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.GetItemRequest;
import ru.practicum.item.model.ItemInfoWithUrlState;
import ru.practicum.item.model.ModifyItemRequest;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

public interface ItemService {

    List<ItemDto> getItems(GetItemRequest req);

    ItemDto updateItem(ModifyItemRequest req) throws IllegalAccessException;

    ItemDto addNewItem(Long userId, ItemDto item);

    void deleteItem(long userId, long itemId) throws IllegalAccessException;

    Collection<ItemInfoWithUrlState> checkItemsUrls(long userId) throws URISyntaxException;
}
