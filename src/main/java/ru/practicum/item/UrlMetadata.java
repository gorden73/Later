package ru.practicum.item;

import java.time.Instant;

interface UrlMetadata {

    String getResolvedUrl();

    String getMimeType();

    String getTitle();

    boolean isHasImage();

    boolean isHasVideo();

    Instant getDateResolved();
}