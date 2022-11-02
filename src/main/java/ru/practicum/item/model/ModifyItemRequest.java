package ru.practicum.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ModifyItemRequest {

    private long userId;

    private long itemId;

    private Set<String> tags;

    private boolean replaceTags;

    private boolean unread;

    public ModifyItemRequest(long userId, long itemId, Set<String> tags, boolean replaceTags, boolean unread) {
        this.userId = userId;
        this.itemId = itemId;
        this.tags = tags;
        this.replaceTags = replaceTags;
        this.unread = unread;
    }
}
