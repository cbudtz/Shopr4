package com.nexb.shopr4.dataModel;

import java.util.ArrayList;

/**
 * @author Christian
 */
public class ForeignUserlist {
    private String UserName = "noName";
    private ArrayList<String> ShopListIDs = new ArrayList<>();

    public ForeignUserlist() {
    }

    public ForeignUserlist(String userName, ArrayList<String> shopListIDs) {
        UserName = userName;
        ShopListIDs = shopListIDs;
    }

    public ArrayList<String> getShopListIDs() {
        return ShopListIDs;
    }

    public void setShopListIDs(ArrayList<String> shopListIDs) {
        ShopListIDs = shopListIDs;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
