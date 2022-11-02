package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.dao.ItemRepository;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.item.model.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UrlMetadataRetrieverImpl urlMetadataRetriever;

    @Transactional
    @Override
    public ItemDto addNewItem(Long userId, ItemDto itemDTO) {
        Item item = ItemMapper.toItem(itemDTO);
        Item result = null;
        Set<String> tags = item.getTags();
        item.setUserId(userId);
        UrlMetadata urlMetadata = urlMetadataRetriever.retrieve(item.getUrl());
        if (urlMetadata != null) {
            if (urlMetadata.getResolvedUrl() != null) {
                Item itemDb = itemRepository.findItemByUserIdAndResolvedUrl(userId, urlMetadata.getResolvedUrl());
                if (itemDb != null) {
                    Set<String> tagsDb = itemDb.getTags();
                    tagsDb.addAll(tags);
                    itemDb.setTags(tagsDb);
                    result = itemDb;
                } else {
                    item.setResolvedUrl(urlMetadata.getResolvedUrl());
                    item.setMimeType(urlMetadata.getMimeType());
                    item.setTitle(urlMetadata.getTitle());
                    item.setHasImage(urlMetadata.isHasImage());
                    item.setHasVideo(urlMetadata.isHasVideo());
                    item.setDateResolved(urlMetadata.getDateResolved());
                }
            }
        }
        if (result == null) {
            log.info("Добавлена ссылка {} пользователя id{}.", item.getUrl(), userId);
            result = itemRepository.save(item);
        }
        return ItemMapper.toDto(result);
    }

    @Override
    public ItemDto updateItem(ModifyItemRequest req) throws IllegalAccessException {
        Item item = itemRepository.getById(req.getItemId());
        if (item.getUserId() != req.getUserId()) {
            throw new IllegalAccessException("У пользователя нет доступа для редактирования.");
        }
        if (req.isReplaceTags()) {
            item.setTags(req.getTags());
        } else {
            item.getTags().addAll(req.getTags());
        }
        item.setUnread(req.isUnread());
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void deleteItem(long userId, long itemId) throws IllegalAccessException {
        Item item = itemRepository.findItemById(itemId).orElseThrow(() -> new IllegalArgumentException("Такой вещи " +
                "нет."));
        if (item.getUserId() != userId) {
            throw new IllegalAccessException("Пользователь не может удалить вещь.");
        }
        log.info("Удалена ссылка {} пользователя id{}.", itemId, userId);
        itemRepository.deleteByUserIdAndId(userId, itemId);
    }

    @Override
    public Collection<ItemInfoWithUrlState> checkItemsUrls(long userId) throws URISyntaxException {
        return itemRepository.checkItemsUrls(userId);
    }

    @Override
    public List<ItemDto> getItems(GetItemRequest req) {
        QItem item = QItem.item;
        List<BooleanExpression> conditions = new ArrayList<>();
        conditions.add(item.userId.eq(req.getUserId()));
        GetItemRequest.State state = req.getState();
        if (!state.equals(GetItemRequest.State.ALL)) {
            conditions.add(makeStateCondition(state));
        }
        GetItemRequest.ContentType contentType = req.getContentType();
        if (!contentType.equals(GetItemRequest.ContentType.ALL)) {
            conditions.add(makeContentTypeCondition(contentType));
        }
        if (req.hasTags()) {
            conditions.add(item.tags.any().in(req.getTags()));
        }
        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();
        Sort sort = makeOrderByClause(req.getSort());
        PageRequest pageRequest = PageRequest.of(0, req.getLimit(), sort);
        Iterable<Item> items = itemRepository.findAll(finalCondition, pageRequest);
        List<ItemDto> dtoList = new ArrayList<>();
        items.forEach(i -> dtoList.add(ItemMapper.toDto(i)));
        return dtoList;
    }

    private BooleanExpression makeStateCondition(GetItemRequest.State state) {
        if (state.equals(GetItemRequest.State.READ)) {
            return QItem.item.unread.isFalse();
        } else {
            return QItem.item.unread.isTrue();
        }
    }

    private BooleanExpression makeContentTypeCondition(GetItemRequest.ContentType contentType) {
        if (contentType.equals(GetItemRequest.ContentType.ARTICLE)) {
            return QItem.item.mimeType.eq("text");
        } else if (contentType.equals(GetItemRequest.ContentType.IMAGE)) {
            return QItem.item.mimeType.eq("image");
        } else {
            return QItem.item.mimeType.eq("video");
        }
    }

    private Sort makeOrderByClause(GetItemRequest.Sort sort) {
        switch (sort) {
            case TITLE:
                return Sort.by("title").ascending();
            case SITE:
                return Sort.by("resolvedUrl").ascending();
            case OLDEST:
                return Sort.by("dateResolved").ascending();
            case NEWEST:
            default:
                return Sort.by("dateResolved").descending();
        }
    }
}
