package com.nexb.shopr4.dataModel;

import java.util.ArrayList;

/**
 * @author Christian Created on 08-11-2015.
 */
public class ShopList {
    private String name = "new shoplist";
    private String id = "";
    private String createdByID ="";
    private ArrayList<ListItem> items = new ArrayList<>();

    public ShopList(){

    }


    //Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedByID() {
        return createdByID;
    }

    public void setCreatedByID(String createdByID) {
        this.createdByID = createdByID;
    }

    public ArrayList<ListItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ListItem> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
