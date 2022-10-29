package ru.practicum.item;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemInfoWithUrlState implements ItemInfo {

    private ItemInfo itemInfo;

    private Boolean state;

    public ItemInfoWithUrlState(ItemInfo itemInfo, Boolean state) {
        this.itemInfo = itemInfo;
        this.state = state;
    }

    @Override
    public Long getId() {
        return itemInfo.getId();
    }

    @Override
    public String getUrl() {
        return itemInfo.getUrl();
    }

    @Override
    public Boolean getState() {
        return itemInfo.getState();
    }
}
