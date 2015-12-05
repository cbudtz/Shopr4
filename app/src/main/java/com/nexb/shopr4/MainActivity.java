package com.nexb.shopr4;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.nexb.shopr4.dataModel.DictionaryItem;
import com.nexb.shopr4.dataModel.InstantAutoCompleteTextView;
import com.nexb.shopr4.dataModel.ListItem;
import com.nexb.shopr4.fragments.BuyListFragment;
import com.nexb.shopr4.fragments.EditListFragment;
import com.nexb.shopr4.fragments.ShareListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EditListFragment.OnFragmentInteractionListener, BuyListFragment.OnFragmentInteractionListener, ShareListFragment.OnFragmentInteractionListener{
    public static final boolean DEBUG = true;
    public enum fragmentState {EDIT,BUY,SHARE};
    private fragmentState fragmentType;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private  FragmentManager f = getSupportFragmentManager();

    private NavigationView navigationView;
    public String userMail = "userID";
    public String userName = "userName";

    public FireBaseController getFireBaseController() {
        return fireBaseController;
    }

    public void setFireBaseController(FireBaseController fireBaseController) {
        this.fireBaseController = fireBaseController;
    }

    private FireBaseController fireBaseController;
    private AutoCompleteTextView autoBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DpToPx.density = getResources().getDisplayMetrics().density;

        FireBaseController.setContext(this, getString(R.string.fireBaseUrl));
        fireBaseController= FireBaseController.getI();
        //Setup UI
        setContentView(R.layout.activity_main);
        //setupToolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //setupAutoCompleteBox
        setUpActionBox();
        setupFloatingActionButton();

        //Setup Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Setup Navigation View
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.inflateHeaderView(R.layout.list_nav_header_view);
        //Setup EditListFragment

        f.beginTransaction().replace(R.id.mainContainer, new EditListFragment()).commit();
        fragmentType = fragmentState.EDIT;
        //autoBox.showDropDown();

        FirebaseHandler firebaseHandler = new FirebaseHandler(this, getString(R.string.fireBaseUrl));

    }

    private void setUpActionBox() {
        autoBox = (AutoCompleteTextView) findViewById(R.id.toolbarAutobox);
        autoBox.setThreshold(0);
        autoBox.setSingleLine(true);
        autoBox.setDropDownBackgroundDrawable(getResources().getDrawable(android.R.drawable.alert_light_frame));
        autoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentType == fragmentState.EDIT) {
                    autoBox.showDropDown();
                }
            }
        });
        autoBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (fragmentType == fragmentState.EDIT) {
                    DictionaryItem newItem = (DictionaryItem) parent.getItemAtPosition(position);
                    fireBaseController.addItemToActiveList(newItem.getCategory(), new ListItem(newItem.getAmount(), newItem.getUnit(), newItem.getName()));
                    autoBox.setText("");
                    autoBox.showDropDown();
                }
            }
        });
        autoBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (fragmentType == fragmentState.EDIT) {
                    ListItem newItem = new ListItem(1, "stk", v.getText().toString());
                    fireBaseController.addItemToActiveListNoCategory(newItem);
                    System.out.println(v.getText());
                    v.setText("");
                }
                return true;
            }
        });
        autoBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                }
            }
        });
        //Change
        autoBox.setDropDownBackgroundDrawable(getResources().getDrawable(android.R.drawable.alert_light_frame));
        ArrayAdapter<DictionaryItem> autoAdaptor = new ArrayAdapter<DictionaryItem>(this, android.R.layout.simple_dropdown_item_1line, FireBaseController.getI().getDictionaryStrings());
        autoBox.setAdapter(autoAdaptor);
        FireBaseController.getI().setDictionaryAdapter(autoAdaptor);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_cat) {
            if(fragmentType == fragmentState.EDIT){
                FireBaseController.getI().addCategory("Enter category name");
            }
            return true;
        }
        if(id == R.id.delete_list){
            if(fragmentType == fragmentState.EDIT){
                FireBaseController.getI().deleteActiveList();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int groupId = item.getGroupId();

        if (id == R.id.nav_new_list) {
            System.out.println(fireBaseController.getUser().getOwnLists());
            fireBaseController.createNewShopList();
            System.out.println(fireBaseController.getUser().getOwnLists());


        } else if (id == R.id.nav_share) {
            f.beginTransaction().replace(R.id.mainContainer, new ShareListFragment()).commit();
            fragmentType = fragmentState.SHARE;

        } else {
            if (groupId == 1) {
                FireBaseController.getI().setActiveList(FireBaseController.getI().getUser().getOwnLists().get(id));
            }
            if (groupId == 2){
                FireBaseController.getI().setActiveList(FireBaseController.getI().getUser().getForeignLists().get(id).getShopListIDs().get(0));
            }
            if(fragmentType == fragmentState.SHARE){
                f.beginTransaction().replace(R.id.mainContainer, new EditListFragment()).commit();
                fragmentType = fragmentState.EDIT;
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //Getters and setters
    public NavigationView getNavigationView() {
        return navigationView;
    }
    private void setupFloatingActionButton() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_lock_idle_lock));
        final Animation firstTurn = AnimationUtils.loadAnimation(this, R.anim.first_turn);
        final Animation secTurn = AnimationUtils.loadAnimation(this, R.anim.sec_turn);
        firstTurn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                fab.startAnimation(secTurn);
                if(fragmentType == fragmentState.EDIT) {
                    f.beginTransaction().replace(R.id.mainContainer, new BuyListFragment()).commit();
                    fragmentType = fragmentState.BUY;
                    fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_preferences));
                }else{
                    f.beginTransaction().replace(R.id.mainContainer, new EditListFragment()).commit();
                    fragmentType = fragmentState.EDIT;
                    fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_lock_idle_lock));
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(firstTurn);
            }
        });
    }
    public void hideKeyboard(){
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}
