package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UrlMetadataRetrieverImpl urlMetadataRetriever;

    @Override
    public List<ItemDto> getItems(long userId) {
        log.info("Запрошены ссылки пользователя id{}.", userId);
        return itemRepository.findByUserId(userId)
                .stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(ModifyItemRequest req) throws IllegalAccessException {
        Item item = itemRepository.getById(req.getItemId());
        if (item.getUserId() != req.getUserId()) {
            throw new IllegalAccessException("У пользователя нет доступа для редактирования.");
        }
        if (req.isReplaceTags()) {
            item.setTags(req.getTags());
        } else {
            item.getTags().addAll(req.getTags());
        }
        if (req.isUnread()) {
            item.setUnread(true);
        }
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public List<ItemDto> getItems(GetItemRequest req) {
        // Для поиска ссылок используем QueryDSL чтобы было удобно настраивать разные варианты фильтров
        QItem item = QItem.item;
        // Мы будем анализировать какие фильтры указал пользователь
        // И все нужные условия фильтрации будем собирать в список
        List<BooleanExpression> conditions = new ArrayList<>();
        // Условие, которое будет проверяться всегда - пользователь сделавший запрос
        // должен быть тем же пользователем, что сохранил ссылку
        conditions.add(item.userId.eq(req.getUserId()));

        // Проверяем один из фильтров указанных в запросе - state
        GetItemRequest.State state = req.getState();
        // Если пользователь указал, что его интересуют все ссылки, вне зависимости
        // от состояния, тогда пропускаем этот фильтр. В обратном случае анализируем
        // указанное состояние и формируем подходящее условие для запроса
        if(!state.equals(GetItemRequest.State.ALL)) {
            conditions.add(makeStateCondition(state));
        }

        // Если пользователь указал, что его интересуют ссылки вне зависимости
        // от типа их содержимого, то пропускаем фильтра, иначе анализируем
        // указанный тип контента и формируем соответствующее условие
        GetItemRequest.ContentType contentType = req.getContentType();
        if(!contentType.equals(GetItemRequest.ContentType.ALL)) {
            conditions.add(makeContentTypeCondition(contentType));
        }

        // если пользователя интересуют ссылки с конкретными тэгами,
        // то добавляем это условие в запрос
        if(req.hasTags()) {
            conditions.add(item.tags.any().in(req.getTags()));
        }

        // из всех подготовленных условий, составляем единое условие
        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        // анализируем, какой вариант сортировки выбрал пользователь
        // и какое количество элементов он выбрал для отображения
        Sort sort = makeOrderByClause(req.getSort());
        PageRequest pageRequest = PageRequest.of(0, req.getLimit(), sort);

        // выполняем запрос к базе данных со всеми подготовленными настройками
        // конвертируем результат в DTO и возвращаем контроллеру
        Iterable<Item> items = itemRepository.findAll(finalCondition, pageRequest);
        List<ItemDto> dtoList = new ArrayList<>();
        items.forEach(i -> dtoList.add(ItemMapper.toDto(i)));
        return dtoList;
    }

    @Transactional
    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDTO) {
        Item item = ItemMapper.toItem(itemDTO);
        Item result = null;
        Set<String> tags = item.getTags();
        item.setUserId(userId);
        Item itemDb = itemRepository.findItemByResolvedUrl(item.getResolvedUrl());
        UrlMetadata urlMetadata = urlMetadataRetriever.retrieve(item.getUrl());
        if (urlMetadata != null) {
            if (itemDb != null) {
                if (urlMetadata.getResolvedUrl().equals(itemDb.getResolvedUrl())) {
                    Set<String> tagsDb = itemDb.getTags();
                    tagsDb.addAll(tags);
                    itemDb.setTags(tagsDb);
                    result = itemDb;
                }
            } else {
                item.setResolvedUrl(urlMetadata.getResolvedUrl());
                item.setMimeType(urlMetadata.getMimeType());
                item.setTitle(urlMetadata.getTitle());
                item.setHasImage(urlMetadata.isHasImage());
                item.setHasVideo(urlMetadata.isHasVideo());
                item.setDateResolved(urlMetadata.getDateResolved());
            }
        }
        if (result == null) {
            log.info("Добавлена ссылка {} пользователя id{}.", item.getUrl(), userId);
            result = itemRepository.save(item);
        }
        return ItemMapper.toDto(result);
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

    private BooleanExpression makeStateCondition(GetItemRequest.State state) {
        if(state.equals(GetItemRequest.State.READ)) {
            return QItem.item.unread.isFalse();
        } else {
            return QItem.item.unread.isTrue();
        }
    }

    private BooleanExpression makeContentTypeCondition(GetItemRequest.ContentType contentType) {
        if(contentType.equals(GetItemRequest.ContentType.ARTICLE)) {
            return QItem.item.mimeType.eq("text");
        } else if(contentType.equals(GetItemRequest.ContentType.IMAGE)) {
            return QItem.item.mimeType.eq("image");
        } else {
            return QItem.item.mimeType.eq("video");
        }
    }

    private Sort makeOrderByClause(GetItemRequest.Sort sort) {
        switch (sort) {
            case TITLE: return Sort.by("title").ascending();
            case SITE: return Sort.by("resolvedUrl").ascending();
            case OLDEST: return Sort.by("dateResolved").ascending();
            case NEWEST:
            default: return Sort.by("dateResolved").descending();
        }
    }
}
