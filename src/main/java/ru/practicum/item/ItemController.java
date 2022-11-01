package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Later-User-Id") Long userId,
                             @RequestParam(defaultValue = "unread") String state,
                             @RequestParam(defaultValue = "all") String contentType,
                             @RequestParam(defaultValue = "newest") String sort,
                             @RequestParam(defaultValue = "10") Integer limit,
                             @RequestParam(required = false) List<String> tags) {
        final GetItemRequest req = new GetItemRequest(userId, state, contentType, sort, limit, tags);
        return itemService.getItems(req);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Later-User-Id") Long userId,
                          @PathVariable Long itemId,
                          @RequestBody Set<String> tags,
                          @RequestParam(value = "replaceTags", required = false) boolean replaceTags,
                          @RequestParam(value = "unread", required = false) boolean unread)
            throws IllegalAccessException {
        ModifyItemRequest req = new ModifyItemRequest(userId, itemId, tags, replaceTags, unread);
        return itemService.update(req);
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Later-User-Id") Long userId,
                       @RequestBody ItemDto itemDTO) {
        return itemService.addNewItem(userId, itemDTO);
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