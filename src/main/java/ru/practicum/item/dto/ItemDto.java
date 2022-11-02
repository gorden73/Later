package ru.practicum.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ItemDto {

    private Long id;

    private Long userId;

    private String url;

    private String resolvedUrl;

    private String mimeType;

    private String title;

    private boolean hasImage;

    private boolean hasVideo;

    private boolean unread;

    private String dateResolved;

    private Set<String> tags;
}
