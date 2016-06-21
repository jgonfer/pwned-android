package com.jgonfer.pwned.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jgonfer.pwned.R;
import com.jgonfer.pwned.connection.requests.AllBreachedServicesListRequest;
import com.jgonfer.pwned.fragment.HistoryFragment;
import com.jgonfer.pwned.fragment.PasswordFragment;
import com.jgonfer.pwned.fragment.RankingFragment;
import com.jgonfer.pwned.fragment.SearchFragment;
import com.jgonfer.pwned.utils.RealmHelper;

import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, AllBreachedServicesListRequest.OnLoginResponseListener {

    AllBreachedServicesListRequest mAllBreachedServiceListRequest;

    private CompositeSubscription mCompositeSubscription;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private Fragment displayedFragment = null;
    private int displayedView = R.id.nav_manual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        nvDrawer.setNavigationItemSelectedListener(this);

        setupDrawerContent(nvDrawer);

        displayedView = RealmHelper.getDisplayedView();
        displayView(displayedView);
        setTitleForNavigationView(false);

        /*
        if (mAllBreachedServiceListRequest == null) {
            mAllBreachedServiceListRequest = new AllBreachedServicesListRequest(this);
        } else if (mAllBreachedServiceListRequest.getBreachedServiceListRequestCallback() == null) {
            mAllBreachedServiceListRequest.setBreachedServiceListRequestCallback(this);
        }
        mAllBreachedServiceListRequest.getAllBreachedServices(this);
        */
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        RealmHelper.setDisplayedView(displayedView);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }



    public void selectDrawerItem(MenuItem menuItem) {
        displayedView = menuItem.getItemId();
        RealmHelper.setDisplayedView(displayedView);
        displayedFragment = null;
        displayView(menuItem.getItemId());

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());

        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    public void displayView(int itemId) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Class fragmentClass;
        FragmentManager fragmentManager = getSupportFragmentManager();
        int fragmentstoPopBackCounter = fragmentManager.getBackStackEntryCount() - 1;
        String fragmentTitle = getResources().getString(R.string.nav_menu_search_title);

        switch(itemId) {
            case R.id.nav_manual:
                fragmentClass = SearchFragment.class;
                fragmentstoPopBackCounter++;
                break;
            case R.id.nav_ranking:
                fragmentClass = RankingFragment.class;
                fragmentTitle = getResources().getString(R.string.nav_menu_ranking_title);
                break;
            case R.id.nav_password:
                fragmentClass = PasswordFragment.class;
                fragmentTitle = getResources().getString(R.string.nav_menu_password_title);
                break;
            case R.id.nav_donate:
                fragmentClass = PasswordFragment.class;
                fragmentTitle = getResources().getString(R.string.nav_menu_donate_title);
                break;
            default:
                fragmentClass = SearchFragment.class;
        }

        if (displayedFragment == null) {
            try {
                displayedFragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Insert the fragment by replacing any existing fragment
        for (int i = 0; i < fragmentstoPopBackCounter; ++i) {
            fragmentManager.popBackStack();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, displayedFragment)
                .addToBackStack(fragmentTitle)
                .commit();

        setTitleForNavigationView(false);
    }

    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        return this.mCompositeSubscription;
    }

    private void setTitleForNavigationView(boolean isBackPressed) {
        if (isBackPressed) {
            switch (displayedView) {
                case R.id.nav_manual:
                    setTitle(getResources().getString(R.string.nav_menu_search_title));
                    break;
                case R.id.nav_ranking:
                    setTitle(getResources().getString(R.string.nav_menu_ranking_title));
                    break;
                case R.id.nav_password:
                    setTitle(getResources().getString(R.string.nav_menu_password_title));
                    break;
                case R.id.nav_donate:
                    setTitle(getResources().getString(R.string.nav_menu_donate_title));
                    break;
            }
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
                String name = entry.getName();
                setTitle(name);
            }
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        setTitleForNavigationView(false);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int backStackCounter = getSupportFragmentManager().getBackStackEntryCount();
            switch (backStackCounter) {
                case 1:
                    System.exit(1);
                    break;
                case 2:
                    nvDrawer.getMenu().getItem(0).setChecked(true);
                    displayedView = R.id.nav_manual;
                    setTitleForNavigationView(true);
                    break;
            }
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
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_manual:
                fragmentClass = SearchFragment.class;
                break;
            case R.id.nav_ranking:
                fragmentClass = HistoryFragment.class;
                break;
            case R.id.nav_password:
                fragmentClass = PasswordFragment.class;
                break;
            default:
                fragmentClass = SearchFragment.class;
        }

        try {
            displayedFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, displayedFragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onAllBreachedServicesListResponse() {
        Log.d(MainActivity.class.getSimpleName(), "onAllBreachedServicesListResponse()");

        //refreshListViewData(true);
    }

    @Override
    public void onAllBreachedServicesListErrorResponse(String errorMessage) {
        Log.d(MainActivity.class.getSimpleName(), "onAllBreachedServicesListErrorResponse(): " + errorMessage);
    }
}
