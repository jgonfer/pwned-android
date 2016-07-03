package com.jgonfer.pwned.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.View;
import android.widget.FrameLayout;

import com.jgonfer.pwned.R;
import com.jgonfer.pwned.connection.requests.AllBreachedServicesListRequest;
import com.jgonfer.pwned.fragment.PasswordFragment;
import com.jgonfer.pwned.fragment.RankingFragment;
import com.jgonfer.pwned.fragment.SearchFragment;
import com.jgonfer.pwned.fragment.SpecialListFragment;
import com.jgonfer.pwned.utils.RealmHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseActivity
        implements AllBreachedServicesListRequest.OnLoginResponseListener, SearchFragment.SearchFragmentListener, DrawerLayout.DrawerListener {

    @BindView(R.id.flContent)
    FrameLayout bodyContainer;
    
    private CompositeSubscription mCompositeSubscription;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private Fragment displayedFragment = null;
    private int displayedView = R.id.nav_manual, mShortAnimationDuration;

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        unbinder = ButterKnife.bind(this);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(this);
        toggle.syncState();

        nvDrawer = (NavigationView) findViewById(R.id.nav_view);

        setupDrawerContent(nvDrawer);

        displayedView = RealmHelper.getDisplayedView();
        displayView(displayedView, false);
        setTitleForNavigationView(false);
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



    public void selectDrawerItem(final MenuItem menuItem) {
        displayedView = menuItem.getItemId();
        RealmHelper.setDisplayedView(displayedView);
        displayedFragment = null;
        displayView(menuItem.getItemId(), true);

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());

        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    public void displayView(int itemId, boolean isDrawerItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Class fragmentClass;
        boolean isNavigation = false;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        String title = setTitleForNavigationView(false);
        Fragment fragmentRecovered = fragmentManager.findFragmentByTag(title);
        if (fragmentRecovered == null || isDrawerItem) {
            int fragmentstoPopBackCounter = fragmentManager.getBackStackEntryCount() - 1;
            String fragmentTitle = getResources().getString(R.string.nav_menu_search_title);
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

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
                case R.string.nav_sensitive_title:
                    fragmentClass = SpecialListFragment.class;
                    fragmentTitle = getResources().getString(R.string.nav_sensitive_title);
                    isNavigation = true;
                    mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    break;
                case R.string.nav_retired_title:
                    fragmentClass = SpecialListFragment.class;
                    fragmentTitle = getResources().getString(R.string.nav_retired_title);
                    isNavigation = true;
                    mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    break;
                default:
                    fragmentClass = SearchFragment.class;
            }

            if (displayedFragment == null) {
                try {
                    displayedFragment = (Fragment) fragmentClass.newInstance();
                    if (displayedFragment instanceof SearchFragment) {
                        ((SearchFragment) displayedFragment).setListener(this);
                    } else if (displayedFragment instanceof SpecialListFragment) {
                        boolean isSensitive = itemId == R.string.nav_sensitive_title;
                        ((SpecialListFragment) displayedFragment).setSpecialType(isSensitive);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            final String finalFragmentTitle = fragmentTitle;
            final String finalTitle = title;
            final int finalFragmentstoPopBackCounter = fragmentstoPopBackCounter;
            final boolean finalIsNavigation = isNavigation;
            setTitle("");
            bodyContainer.animate()
                    .alpha(0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // Insert the fragment by replacing any existing fragment
                            try {
                                for (int i = 0; i < finalFragmentstoPopBackCounter; ++i) {
                                    fragmentManager.popBackStack();
                                }

                                if (finalIsNavigation) {
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                    fragmentManager.beginTransaction()
                                            .add(R.id.flContent, displayedFragment, finalTitle)
                                            .addToBackStack(finalFragmentTitle)
                                            .commit();
                                } else {
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.flContent, displayedFragment, finalTitle)
                                            .addToBackStack(finalFragmentTitle)
                                            .commit();
                                }
                                setTitle(finalFragmentTitle);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            bodyContainer.animate()
                                    .alpha(1f)
                                    .setDuration(mShortAnimationDuration)
                                    .setListener(null);
                        }
                    });
        }
    }

    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        return this.mCompositeSubscription;
    }

    private String setTitleForNavigationView(boolean isBackPressed) {
        String title = getResources().getString(R.string.nav_menu_search_title);
        if (isBackPressed) {
            switch (displayedView) {
                case R.id.nav_manual:
                    title = getResources().getString(R.string.nav_menu_search_title);
                    setTitle(title);
                    break;
                case R.id.nav_ranking:
                    title = getResources().getString(R.string.nav_menu_ranking_title);
                    setTitle(title);
                    break;
                case R.id.nav_password:
                    title = getResources().getString(R.string.nav_menu_password_title);
                    setTitle(title);
                    break;
                case R.id.nav_donate:
                    title = getResources().getString(R.string.nav_menu_donate_title);
                    setTitle(title);
                    break;
                case R.string.nav_sensitive_title:
                    title = getResources().getString(R.string.nav_sensitive_title);
                    setTitle(title);
                    break;
                case R.string.nav_retired_title:
                    title = getResources().getString(R.string.nav_retired_title);
                    setTitle(title);
                    break;
            }
        } else {
            switch (displayedView) {
                case R.string.nav_sensitive_title:
                    title = getResources().getString(R.string.nav_sensitive_title);
                    setTitle(title);
                    break;
                case R.string.nav_retired_title:
                    title = getResources().getString(R.string.nav_retired_title);
                    setTitle(title);
                    break;
                default:
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        FragmentManager.BackStackEntry entry = getSupportFragmentManager()
                                .getBackStackEntryAt(
                                        getSupportFragmentManager().getBackStackEntryCount() - 1);
                        title = entry.getName();
                        setTitle(title);
                    }
                    break;
            }
        }
        return title;
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
                    setTitle("");
                    bodyContainer.animate()
                            .alpha(0f)
                            .setDuration(mShortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    nvDrawer.getMenu().getItem(0).setChecked(true);
                                    displayedView = R.id.nav_manual;
                                    setTitleForNavigationView(true);
                                    MainActivity.super.onBackPressed();

                                    bodyContainer.animate()
                                            .alpha(1f)
                                            .setDuration(mShortAnimationDuration)
                                            .setListener(null);
                                }
                            });
                    break;
                default:
                    super.onBackPressed();
                    break;
            }
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

    @Override
    public void onAllBreachedServicesListResponse() {
        Log.d(MainActivity.class.getSimpleName(), "Success!");
    }

    @Override
    public void onAllBreachedServicesListErrorResponse(String errorMessage) {
        Log.d(MainActivity.class.getSimpleName(), "Error: " + errorMessage);
    }

    @Override
    public void specialBreachedServices(boolean isSensitive) {
        int itemId = isSensitive ? R.string.nav_sensitive_title : R.string.nav_retired_title;
        displayedView = itemId;
        RealmHelper.setDisplayedView(displayedView);
        displayedFragment = null;
        displayView(itemId, false);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        Log.d("Hola", "onDrawerOpened");
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        Log.d("Hola", "onDrawerClosed");
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        Log.d("Hola", "onDrawerStateChanged");
    }
}