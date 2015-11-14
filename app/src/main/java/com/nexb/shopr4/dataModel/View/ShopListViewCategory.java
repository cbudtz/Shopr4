package com.nexb.shopr4.dataModel.View;

import com.nexb.shopr4.dataModel.ShopListViewContent;

/**
 * Created by Christian on 14-11-2015.
 */
public class ShopListViewCategory extends ShopListViewContent {
    private String name;

    public ShopListViewCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
