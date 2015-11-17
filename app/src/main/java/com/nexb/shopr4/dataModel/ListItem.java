package com.nexb.shopr4.dataModel;

/**
 * Created by Christian on 08-11-2015.
 */
public class ListItem {
    private double amount;
    private String unit;
    private String name;

    public ListItem(){}

    public ListItem(double amount, String unit, String name) {
        this.amount = amount;
        this.unit = unit;
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
