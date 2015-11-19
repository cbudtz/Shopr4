package com.nexb.shopr4.dataModel;

import java.util.Date;

/**
 * Created by Christian on 15-11-2015.
 */
public class DictionaryItem {


    private String name ="Test";



    private String unit = "stk";
    private double amount = 1.0;
    private Date lastUsed = new Date();
    private double frequencyOfUse = 1;

    public DictionaryItem(){

    }

    public DictionaryItem(String name, String unit, double amount) {
        this.name = name;
        this.unit = unit;
        this.amount = amount;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public double getFrequencyOfUse() {
        return frequencyOfUse;
    }

    public void setFrequencyOfUse(double frequencyOfUse) {
        this.frequencyOfUse = frequencyOfUse;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
