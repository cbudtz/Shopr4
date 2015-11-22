package com.nexb.shopr4;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nexb.shopr4.dataModel.InstantAutoCompleteTextView;
import com.nexb.shopr4.dataModel.ListItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EditListFragment.OnFragmentInteractionListener, BuyListFragment.OnFragmentInteractionListener{
    public static final boolean DEBUG = true;

    private Toolbar toolbar;
    private EditListFragment editListFragment;
    private BuyListFragment buyListFragment;
    private boolean shownFragment = false;
    private FloatingActionButton fab;
    private  FragmentManager f = getSupportFragmentManager();


    private NavigationView navigationView;

    public FireBaseController getFireBaseController() {
        return fireBaseController;
    }

    public void setFireBaseController(FireBaseController fireBaseController) {
        this.fireBaseController = fireBaseController;
    }

    private FireBaseController fireBaseController;
    private InstantAutoCompleteTextView autoBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup Firebase
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

        //Setup EditListFragment
        editListFragment = new EditListFragment();
        f.beginTransaction().replace(R.id.mainContainer, editListFragment).commit();
        shownFragment = true;
        //autoBox.showDropDown();

    }

    private void setUpActionBox() {
        autoBox = (InstantAutoCompleteTextView) findViewById(R.id.toolbarAutobox);
        autoBox.setThreshold(0);
        autoBox.setSingleLine(true);
        autoBox.setDropDownBackgroundDrawable(getResources().getDrawable(android.R.drawable.alert_light_frame));
        autoBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem newItem = new ListItem(1, " ", parent.getItemAtPosition(position).toString()); //Item at position should return some values from the dictionary
                //TODO: Find some way to keep track of autobox values!
                System.out.println("view Clicked:" + view);
                System.out.println("parent.getItemAtPosition " + parent.getItemAtPosition(position));
                //find item contents.
                fireBaseController.addItemToActiveList("Test",new ListItem(1, "stk" , "TestBanan"));
                autoBox.setText("");
                autoBox.showDropDown();
            }
        });
        autoBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    ListItem newItem = new ListItem(1, " ", v.getText().toString());
                    fireBaseController.addItemToActiveListNoCategory(newItem);
                }
                System.out.println(v.getText());
                v.setText("");
                return true;
            }
        });
        autoBox.setDropDownBackgroundDrawable(getResources().getDrawable(android.R.drawable.alert_light_frame));
        ArrayAdapter<String> autoAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, FireBaseController.getI().getDictionaryStrings());
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
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_list) {
            System.out.println(fireBaseController.getUser().getOwnLists());
            fireBaseController.createNewShopList();
           // Snackbar.make(view, "Created new shoplist with id: " + fireBaseController.getUser().getOwnLists().get(fireBaseController.getUser().getOwnLists().size()-1), Snackbar.LENGTH_LONG)
             //       .setAction("Action", null).show();
            System.out.println(fireBaseController.getUser().getOwnLists());


        } else if (id == R.id.nav_share) {


        } else {
            FireBaseController.getI().setActiveList(FireBaseController.getI().getUser().getOwnLists().get(id));
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
                if(shownFragment) {
                    buyListFragment = new BuyListFragment();
                    f.beginTransaction().replace(R.id.mainContainer, buyListFragment).commit();
                    shownFragment = false;
                    fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_preferences));
                }else{
                    editListFragment = new EditListFragment();
                    f.beginTransaction().replace(R.id.mainContainer, editListFragment).commit();
                    shownFragment = true;
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
}
