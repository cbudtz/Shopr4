package com.nexb.shopr4;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Christian on 19-11-2015.
 */
public class DictionaryAdaptor<String> extends ArrayAdapter {
    public DictionaryAdaptor(MainActivity mainActivity, int simple_dropdown_item_1line, ArrayList<String> dictionaryItems) {
        super(mainActivity, simple_dropdown_item_1line);

    }
}
