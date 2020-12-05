package com.jrinfolab.beautyshop.pojo;

import java.util.List;

public class PriceGroup {

    String groupName;
    List<PriceItem> priceItemList;

    public PriceGroup(String name, List<PriceItem> priceList) {
        this.groupName = name;
        this.priceItemList = priceList;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<PriceItem> getPriceItemList() {
        return priceItemList;
    }

    public void setPriceItemList(List<PriceItem> priceItemList) {
        this.priceItemList = priceItemList;
    }
}
