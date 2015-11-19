package com.nexb.shopr4.dataModel.View;

/**
 * Created by Christian on 14-11-2015.
 */
public class ShopListViewItem extends ShopListViewContent {

    private String unit;
    private String name;
    private double amount;

    public ShopListViewItem(double amount, String unit, String name) {
        super();
        this.amount = amount;
        this.unit = unit;
        this.name = name;

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
    public contentType getType() {
        return contentType.ITEM;
    }
}
