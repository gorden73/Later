package ru.practicum.item.model;

import java.time.Instant;

public interface UrlMetadata {

    String getResolvedUrl();

    String getMimeType();

    String getTitle();

    boolean isHasImage();

    boolean isHasVideo();

    Instant getDateResolved();
}