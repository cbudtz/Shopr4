package com.nexb.shopr4;

import android.app.Activity;

import com.nexb.shopr4.dataModel.Category;
import com.nexb.shopr4.dataModel.ListItem;

/**
 * Created by Christian on 03-12-2015.
 */
public interface IDataBaseController {
    //DataBaseConnectionSetup

    void setContext(Activity mainActivity, String dataBaseUrl);
    void init();
    //CUD operations
    String createNewShopList();
    void setActiveList(String ShopListID);
    void setActiveShopListName(String shopListName);
    void deleteList(String shopListID);

    void addCategory(String name);
    void updateCategoryName(String oldName, String newName);
    void deleteCategory(int catID);
    void insertCategory(int catID, Category Category);

    void addItemToActiveList(String categoryName, ListItem listItem);
    void deleteItem(String catID, int itemID);
    void deleteItem(String catID, String itemName);




    //ShopListListening

}
