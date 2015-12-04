package com.nexb.shopr4.dataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Christian on 04-12-2015.
 */
public class SuperMarket {
    LinkedHashMap<String, ArrayList<DictionaryItem>> categories = new LinkedHashMap<>();

    public void addCategory(String categoryName){
        categories.put(categoryName, new ArrayList<DictionaryItem>());
    }
    public void addItemToCategory(String categoryName, DictionaryItem dictionaryItem){
        ArrayList<DictionaryItem> categoryList = categories.get(categoryName);
        if (categoryList != null){
            categoryList.add(dictionaryItem);
        }
    }

}
