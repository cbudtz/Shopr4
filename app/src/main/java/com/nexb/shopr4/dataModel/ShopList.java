package com.nexb.shopr4.dataModel;

import java.util.ArrayList;

/**
 * @author Christian Created on 08-11-2015.
 */
public class ShopList {
    private String name = "new shoplist";
    private String id = "";
    private String createdByID ="";
    private ArrayList<Category> categories = new ArrayList<>();

    public ShopList(){
        //categories.add(new Category("No Category"));
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

    public ArrayList<Category> getCategories() {
        return categories;
    }
    public void setCategories(ArrayList<Category> items) {
        this.categories = items;
    }
    public void addCategory(Category c){
        categories.add(c);
    }
    public void removeCategory(Category c){
        categories.remove(c);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
