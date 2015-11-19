package com.nexb.shopr4.dataModel.View;

import com.nexb.shopr4.dataModel.ListItem;

/**
 * Created by Christian on 14-11-2015.
 */
public class ShopListViewItem extends ShopListViewContent {

    private final int itemId;
    private String unit;
    private String name;
    private double amount;
    private final int categoryID;

    public ShopListViewItem(double amount, String unit, String name, int categoryId, int itemId) {
        super();
        this.amount = amount;
        this.unit = unit;
        this.name = name;
        this.categoryID = categoryId;
        this.itemId = itemId;


    }

    public String getName() {
        return name;
    }
    public String getUnit() {
        return unit;
    }
    public double getAmount() {
        return amount;
    }
    @Override
    public contentType getType() {        return contentType.ITEM;    }

    public int getItemId() {        return itemId;    }

    public int getCategoryID() {        return categoryID;    }
}
