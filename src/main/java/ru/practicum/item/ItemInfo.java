package ru.practicum.item;

import lombok.Getter;
import lombok.Setter;

public interface ItemInfo {
    Long getId();
    String getUrl();
    Boolean getState();
}
