package ru.practicum.item;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ItemMapper {
    public static ItemDto toDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setUrl(item.getUrl());
        dto.setResolvedUrl(item.getResolvedUrl());
        dto.setMimeType(item.getMimeType());
        dto.setTitle(item.getTitle());
        dto.setHasImage(item.isHasImage());
        dto.setHasVideo(item.isHasVideo());
        dto.setUnread(item.isUnread());
        dto.setDateResolved(item.getDateResolved().toString());
        dto.setTags(item.getTags());
        return dto;
    }

    public static Item toItem(ItemDto dto) {
        return new Item(
                dto.getUserId(),
                dto.getUrl(),
                dto.getTags());
    }
}
