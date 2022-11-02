package ru.practicum.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ItemInfoWithUrlState implements ItemInfo {

    @JsonIgnore
    private ItemInfo itemInfo;

    private String url;

    private HttpStatus status;

    public ItemInfoWithUrlState(ItemInfo itemInfo, HttpStatus status) {
        this.itemInfo = itemInfo;
        this.url = getUrl();
        this.status = status;
    }

    @Override
    public Long getId() {
        return itemInfo.getId();
    }

    @Override
    public String getUrl() {
        return itemInfo.getUrl();
    }
}
