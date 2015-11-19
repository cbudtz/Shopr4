package com.nexb.shopr4;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.nexb.shopr4.dataModel.Category;
import com.nexb.shopr4.dataModel.DictionaryItem;
import com.nexb.shopr4.dataModel.ListItem;
import com.nexb.shopr4.dataModel.ShopList;
import com.nexb.shopr4.dataModel.View.ShopListViewContent;
import com.nexb.shopr4.dataModel.User;
import com.nexb.shopr4.dataModel.View.ShopListViewCategory;
import com.nexb.shopr4.dataModel.View.ShopListViewItem;

import java.util.ArrayList;

/**
 * Created by Christian on 12-11-2015.
 */
public class FireBaseController {
    public static FireBaseController instance;

    private static MainActivity activity;
    private static String url;
    //Folders
    private Firebase firebaseRoot;
    private Firebase firebaseUserDir;
    private Firebase firebaseShopListDir;


    //User data:
    private User user = new User();
    private Firebase firebaseUserRef;

    //Active list
    private Firebase activeListRef;
    private ShopList activeShopList = new ShopList();

    //ArrayAdaptor to be notified on dataChange
    private ArrayAdapter<ShopListViewContent> shoplistAdaptor;
    private ArrayAdapter<String> dictionaryAdapter;



    //ArrayList
    private ArrayList<ShopListViewContent> shoplistViewContents;
    private ValueEventListener activeListListener;

    //Initialization Methods ---------------------
    public static void setContext(MainActivity mainActivity, String Firebaseurl){
        activity = mainActivity;
        url = Firebaseurl;
    }

    public static synchronized FireBaseController getI() {
        if (activity == null || url == null) {
            System.out.println("Must provide url and activity first!!");
            return null;
        }
        if (instance == null) {
            instance = new FireBaseController(activity, url);
            instance.init();
        }
        return instance;

    }

    private FireBaseController(MainActivity mainActivity, String url) {
        this.activity = mainActivity;
        this.url = url;
        Firebase.setAndroidContext(activity);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }

    public void init() {
        firebaseRoot = new Firebase(url);
        firebaseUserDir = firebaseRoot.child(activity.getString(R.string.userDir));
        firebaseShopListDir = firebaseRoot.child(activity.getString(R.string.shopListDir));
        resolveUser();
    }

    private void resolveUser() {
        //Find user in android accounts
        //resolve UserID
        user = new User(); //Initializes to new User
        AccountManager manager = (AccountManager) activity.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] list = manager.getAccountsByType("com.google");
        if (list!=null && list.length>0 && list[0]!=null) {
            String id = list[0].name;
            id = id.replace('.',':');
            System.out.println(id);
            user.setUserID(id);
        }
        else {

            user.setUserID("" + Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));

        }
        System.out.println("UserID: " + user.getUserID());
        // Listen to database for changes in User!
        firebaseUserRef = firebaseUserDir.child(user.getUserID());
        firebaseUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("User data changed!");
                User userFromFirebase = dataSnapshot.getValue(User.class);
                if (userFromFirebase == null || userFromFirebase.getUserName() == null) {
                    firebaseUserRef.setValue(user);
                    System.out.println("User created");
                } else {
                    user = userFromFirebase;
                    System.out.println("User already Exists");
                    System.out.println(user.getActiveList());


                }
                setActiveList(user.getActiveList());
                //Update NavigationDrawer
                activity.getNavigationView().getMenu().removeGroup(1);
                int i = 0;
                for (String s : user.getOwnLists()) {
                    activity.getNavigationView().getMenu().add(1, i, i, s);
                    i++;
                }
                if (dictionaryAdapter!=null) dictionaryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

      //Should happen on callback! --  setActiveList(user.getActiveList());
    }
    //End of initialization ----------------------

    public void setActiveList(String listID){
        //Make certain that user has at least one shopping list...
        if (listID == null) {
            listID = createNewShopList();
        };
        if (activeListRef!=null) activeListRef.removeEventListener(activeListListener);
        //Change location for active list storage
        activeListRef = firebaseShopListDir.child(listID);
        if (MainActivity.DEBUG) System.out.println(activeListRef);
        //Listen to new location
        activeListListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShopList newShopList = dataSnapshot.getValue(ShopList.class);
                activeShopList = newShopList;
                System.out.println("ShopList Changed:" + ((activeShopList != null) ? newShopList.getId() + newShopList.getName() : "null"));
                //Parse for arrayadaptor.
                parseShopList();
                if (shoplistAdaptor != null) shoplistAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        activeListRef.addValueEventListener(activeListListener);


    }

    private void parseShopList() {
        ArrayList<ShopListViewContent> newShopListViewContents = new ArrayList<ShopListViewContent>();
        if (activeShopList==null) return;
        for (Category c: activeShopList.getCategories() ) {
            //add category element
            newShopListViewContents.add(new ShopListViewCategory(c.getName()));
            for (ListItem l:c.getItems()) {
                newShopListViewContents.add(new ShopListViewItem(l.getAmount(),l.getUnit(),l.getName()));
            }
        }
        if (MainActivity.DEBUG){
            for (ShopListViewContent s: newShopListViewContents ) {
                System.out.println("ShoplistViewItem: " + s);

            }
        }
    }

    public String createNewShopList(){
        //Get ref from Firebase
        Firebase newListRef = firebaseShopListDir.push();
        //Create Default list Template
        ShopList newShopList = new ShopList();
        newShopList.setId(newListRef.getKey());
        newShopList.setName("New List");
        newShopList.setCreatedByID(user.getUserID());
        newShopList.addCategory(new Category("No Category"));
        newListRef.setValue(newShopList);
        user.addOwnList(newShopList.getId());
        if (user.getActiveList()==null) user.setActiveList(newShopList.getId());
        firebaseUserRef.setValue(user);
        return newListRef.getKey();
    }

    public void addItemToActiveListNoCategory(ListItem l){
        activeShopList.getCategories().get(0).getItems().add(l);
        activeListRef.setValue(activeShopList);
    }

    public void addItemToActiveList(String category, ListItem l){
        for (Category c : activeShopList.getCategories()) {
            if (c.getName().equalsIgnoreCase(category)){
                //Found category in Shoplist
                c.getItems().add(l);
                return;
            }
        }
        //Category not found - put in new category
        Category newCat = new Category(category);
        activeShopList.getCategories().add(newCat);
        newCat.getItems().add(l);
        activeListRef.setValue(activeShopList);
    }


//TODO: probably unnecessary
  //  public ShopList getActiveShopList() {
     //   return activeShopList;
   // }

    public void setActiveShopList(ShopList activeShopList) {
        this.activeShopList = activeShopList;
    }

    public void setShoplistAdaptor(ArrayAdapter<ShopListViewContent> shoplistAdaptor) {
        this.shoplistAdaptor = shoplistAdaptor;
    }

    public User getUser() {        return user;    }
    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<ShopListViewContent> getShoplistViewContents() {
        if (shoplistViewContents == null) return new ArrayList<ShopListViewContent>();
        return shoplistViewContents;
    }

    public ArrayList<String> getDictionaryStrings() {
        if (MainActivity.DEBUG){
            user.getUserDictionary().add(new DictionaryItem("Bananer", "stk" , 10));
            user.getUserDictionary().add(new DictionaryItem("Ananas", "stk" , 1));
            System.out.println("Adding some items to Dictionary");
        }
        ArrayList<String> dictionaryStrings = new ArrayList<>();
        for (DictionaryItem d : user.getUserDictionary()) {
            dictionaryStrings.add(d.getName() + " - " + d.getAmount() + " " + d.getUnit());

        }
        if (dictionaryAdapter!=null) dictionaryAdapter.notifyDataSetChanged();
        return dictionaryStrings;
    }

    public void setDictionaryAdapter(ArrayAdapter<String> dictionaryAdapter) {
        this.dictionaryAdapter = dictionaryAdapter;
    }
}
