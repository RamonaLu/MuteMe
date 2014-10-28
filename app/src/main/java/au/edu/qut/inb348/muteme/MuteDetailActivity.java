package au.edu.qut.inb348.muteme;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import au.edu.qut.inb348.muteme.data.MutesDbHelper;
import au.edu.qut.inb348.muteme.model.Mute;


public class MuteDetailActivity extends FragmentActivity  {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    TabsPagerAdapter tabsPagerAdapter;
    ViewPager viewPager;
    MuteChronoFragment chronoFragment;
    MuteGeoFragment geoFragment;
    private MuteRegistrar muteRegistrar;


    private Mute item;
    private MutesDbHelper dbHelper;

    public class TabsPagerAdapter extends FragmentStatePagerAdapter {
        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch(i){
                case 0:
                    chronoFragment = new MuteChronoFragment();
                    chronoFragment.mute = item;
                    return chronoFragment;
                case 1:
                    geoFragment = new MuteGeoFragment();
                    geoFragment.mute = item;
                    return geoFragment;
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    @Override
    protected void onPause() {
        saveMute();
        super.onPause();
    }

    private void saveMute() {
        muteRegistrar.deregister(item);
        if (chronoFragment != null) {
            chronoFragment.syncToMute (item);
        }
        if (geoFragment != null) {
            geoFragment.syncToMute (item);
        }
        muteRegistrar.register(item);
        dbHelper.updateMute(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mute_detail);
        tabsPagerAdapter  =
                new TabsPagerAdapter(
                        getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(tabsPagerAdapter);
        dbHelper = new MutesDbHelper(this);
        muteRegistrar = new MuteRegistrar(this);
        item = dbHelper.getMute(getIntent().getLongExtra(MuteDetailActivity.ARG_ITEM_ID, -1));
        // Show the Up button in the action bar.
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            // Specify that tabs should be displayed in the action bar.
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // Create a tab listener that is called when the user changes tabs.
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                    viewPager.setCurrentItem(tab.getPosition());
                    switch(tab.getPosition()){
                        case 0:
                            if (item != null && chronoFragment != null) {
                                chronoFragment.syncFromMute(item);
                            }
                            break;
                        case 1:
                            if (item != null && geoFragment != null) {
                                geoFragment.syncFromMute(item);
                            }
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }

                }

                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    saveMute();
                }

                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

                }

            };
            viewPager.setOnPageChangeListener(
                    new ViewPager.SimpleOnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            // When swiping between pages, select the
                            // corresponding tab.
                            getActionBar().setSelectedNavigationItem(position);
                        }
                    });
            actionBar.addTab(actionBar.newTab().setText("Time").setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("Location").setTabListener(tabListener));

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, MuteListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
