package ru.practicum.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetItemRequest {

    private long userId;

    private State state;

    private ContentType contentType;

    private Sort sort;

    private Integer limit;

    private List<String> tags;

    public GetItemRequest(long userId, String state, String contentType, String sort, Integer limit,
                          List<String> tags) {
        this.userId = userId;
        this.state = State.valueOf(state.toUpperCase());
        this.contentType = ContentType.valueOf(contentType.toUpperCase());
        this.sort = Sort.valueOf(sort.toUpperCase());
        this.limit = limit;
        if (tags != null) {
            this.tags = tags;
        }
    }

    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }

    public enum State {UNREAD, READ, ALL }

    public enum ContentType { ARTICLE, VIDEO, IMAGE, ALL }

    public enum Sort { NEWEST, OLDEST, TITLE, SITE }
}
