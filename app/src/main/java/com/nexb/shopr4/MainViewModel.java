package com.nexb.shopr4;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nexb.shopr4.View.ShopListViewContent;
import com.nexb.shopr4.dataModel.DictionaryItem;
import com.nexb.shopr4.dataModel.ForeignUserlist;
import com.nexb.shopr4.dataModel.ListItem;
import com.nexb.shopr4.dataModel.ShopList;
import com.nexb.shopr4.dataModel.SuperMarket;
import com.nexb.shopr4.dataModel.User;

import java.util.ArrayList;

/**
 * @authour Christian on 04-12-2015.
 */
public class MainViewModel implements IMainViewModel, IUserDataListener, IShopListListener, ISuperMarketListener{

    MainActivity mainActivity;
    IDataBaseController dataBaseController;
    SuperMarket activeSuperMarket;

    //Adaptors to be notified on dataChange
    private ArrayAdapter<ShopListViewContent> shoplistAdaptor;
    private ArrayAdapter<DictionaryItem> dictionaryAdapter;
    private ArrayList<TextView> shopListTitleViews;

    //Views to be updatesd on dataChange
    private NavigationView navigationDrawerView;

    public MainViewModel(MainActivity mainActivity,
            IDataBaseController dataBaseController, ArrayAdapter<ShopListViewContent> shoplistAdaptor,
                         ArrayAdapter<DictionaryItem> dictionaryAdapter, ArrayList<TextView> shopListTitleViews,
                         NavigationView navigationDrawerView) {
        this.mainActivity = mainActivity;
        this.dataBaseController = dataBaseController;
        this.shoplistAdaptor = shoplistAdaptor;
        this.dictionaryAdapter = dictionaryAdapter;
        this.shopListTitleViews = shopListTitleViews;
        this.navigationDrawerView = navigationDrawerView;
    }
//---------------------------------- Input
    //AutoBoxClicks----------------
    @Override
    public void autoBoxClicked() {
        //TODO Some manipulation of autobox? Drop down?
    }

    @Override
    public void autoBoxTextEntered(ListItem listItem) {
        //TODO different behavior in Shop fragment
        dataBaseController.addItemToActiveList("Ingen kategori",listItem);
    }

    @Override
    public void autoBoxItemSelected(DictionaryItem dictionaryItem) {
        dataBaseController.addItemToActiveList(dictionaryItem.getCategory(),new ListItem(dictionaryItem.getAmount(),dictionaryItem.getUnit(),dictionaryItem.getName()));
    }

//----------------------------------- Output
    //CallBacks from database
    @Override
    public void userdataChanged(User user) {
                //Update active shopping list
        mainActivity.userMail = user.getUserID(); //Tell Main activity the users name and email
        mainActivity.userName = user.getUserName();
        if (mainActivity.findViewById(R.id.userMail)!=null) ((TextView)mainActivity.findViewById(R.id.userMail)).setText(user.getUserID().replace(":","."));
        if (mainActivity.findViewById(R.id.userName)!=null) ((TextView)mainActivity.findViewById(R.id.userName)).setText(user.getUserName());
        navigationDrawerView.getMenu().removeGroup(1);  //Own Lists
        navigationDrawerView.getMenu().removeGroup(2); //Foreign Lists

        int i = 0;
        for (String s : user.getOwnLists()) {
            mainActivity.getNavigationView().getMenu().add(1, i, i, s);
            i++;
        }
        for (ForeignUserlist s : user.getForeignLists()){
            if (s!=null && s.getShopListIDs()!=null && s.getShopListIDs().size()>0) {
                mainActivity.getNavigationView().getMenu().add(2, i, i, s.getShopListIDs().get(0));
                i++;
            }
        }

        View header = LayoutInflater.from(mainActivity).inflate(R.layout.nav_header_main, null);
        boolean standardized = false;

        if (dictionaryAdapter != null) {
            dictionaryAdapter.clear();
            dictionaryAdapter.addAll(getDictionaryStrings(user));
            dictionaryAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void shopListDataChanged(ShopList shopList) {

    }

    @Override
    public void superMarketChanged(SuperMarket superMarket) {

    }

    //

    public ArrayList<DictionaryItem> getDictionaryStrings(User user) {
        if (MainActivity.DEBUG){
//            user.getUserDictionary().add(new DictionaryItem("Bananer", "stk" , 10));
//            user.getUserDictionary().add(new DictionaryItem("Ananas", "stk" , 1));
//            System.out.println("Adding some items to Dictionary");
        }
        ArrayList<String> dictionaryStrings = new ArrayList<>();
        for (DictionaryItem d : user.getUserDictionary()) {
            dictionaryStrings.add(d.getName() + " - " + d.getAmount() + " " + d.getUnit());

        }
        if (dictionaryAdapter!=null) dictionaryAdapter.notifyDataSetChanged();
        return user.getUserDictionary();
    }
}
