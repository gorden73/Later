package ru.practicum.item;

import ru.practicum.item.model.UrlMetadata;

public interface UrlMetadataRetriever {

    UrlMetadata retrieve(String urlString);
}
