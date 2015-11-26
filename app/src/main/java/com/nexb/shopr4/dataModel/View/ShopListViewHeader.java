package com.nexb.shopr4.dataModel.View;

import com.nexb.shopr4.dataModel.ListItem;

/**
 * Created by mac on 26/11/15.
 */
public class ShopListViewHeader extends ShopListViewContent{

    private String name;

    public ShopListViewHeader(String name) {
        super();
        this.name = name;
    }

    @Override
    public contentType getType() {
        return contentType.ITEM;
    }

    public String getName() {
        return name;
    }

}
