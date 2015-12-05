package com.nexb.shopr4;

import com.nexb.shopr4.dataModel.DictionaryItem;
import com.nexb.shopr4.dataModel.ListItem;

/**
 * @author Christian on 04-12-2015.
 */
public interface IMainViewModel extends IShopListListener, ISuperMarketListener, IUserDataListener{
    void autoBoxClicked();
    void autoBoxTextEntered(ListItem listItem);
    void autoBoxItemSelected(DictionaryItem dictionaryItem);




}
