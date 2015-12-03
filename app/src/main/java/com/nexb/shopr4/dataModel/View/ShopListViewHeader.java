package com.nexb.shopr4.dataModel.View;

import com.nexb.shopr4.dataModel.ListItem;

/**
 * Created by mac on 26/11/15.
 */
public class ShopListViewHeader extends ShopListViewContent{

    private String name;

    public ShopListViewHeader() {
        super();
    }

    @Override
    public contentType getType() {
        return contentType.HEADER;
    }

    public String getName() {
        return name;
    }

}