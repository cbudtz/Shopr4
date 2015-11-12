package com.nexb.shopr4;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.nexb.shopr4.dataModel.Category;
import com.nexb.shopr4.dataModel.ListItem;
import com.nexb.shopr4.dataModel.ShopList;
import com.nexb.shopr4.dataModel.User;

/**
 * Created by Christian on 12-11-2015.
 */
public class FireBaseController {
    private final AppCompatActivity activity;
    private final String url;
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

    public FireBaseController(AppCompatActivity mainActivity, String url) {
        this.activity = mainActivity;
        this.url = url;
        Firebase.setAndroidContext(activity);
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
        // Listen to database!
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

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        setActiveList(user.getActiveList());
        activeListRef.setValue(new ShopList());

    }

    private void setActiveList(String listID){
        activeListRef = firebaseShopListDir.child(listID);
        activeListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShopList newShopList = dataSnapshot.getValue(ShopList.class);
                activeShopList=newShopList;
                System.out.println("ShopList Changed:" + ((activeShopList!=null) ? newShopList:"null"));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    public ShopList getActiveShopList() {
        return activeShopList;
    }

    public void setActiveShopList(ShopList activeShopList) {
        this.activeShopList = activeShopList;
    }
}
