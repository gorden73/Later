package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.GetItemRequest;
import ru.practicum.item.model.ItemInfoWithUrlState;
import ru.practicum.item.model.ModifyItemRequest;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Later-User-Id") Long userId,
                           @RequestBody ItemDto itemDTO) {
        return itemService.addNewItem(userId, itemDTO);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Later-User-Id") Long userId,
                                  @RequestParam(defaultValue = "unread") String state,
                                  @RequestParam(defaultValue = "all") String contentType,
                                  @RequestParam(defaultValue = "newest") String sort,
                                  @RequestParam(defaultValue = "10") Integer limit,
                                  @RequestParam(required = false) List<String> tags) {
        final GetItemRequest req = new GetItemRequest(userId, state, contentType, sort, limit, tags);
        return itemService.getItems(req);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Later-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody Set<String> tags,
                              @RequestParam(value = "replaceTags", required = false) boolean replaceTags,
                              @RequestParam(value = "unread", required = false) boolean unread)
            throws IllegalAccessException {
        ModifyItemRequest req = new ModifyItemRequest(userId, itemId, tags, replaceTags, unread);
        return itemService.updateItem(req);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Later-User-Id") Long userId,
                           @PathVariable Long itemId) throws IllegalAccessException {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/status")
    public Collection<ItemInfoWithUrlState> checkUrlsByUserId(@RequestHeader("X-Later-User-Id") Long userId)
            throws URISyntaxException {
        return itemService.checkItemsUrls(userId);
    }
}