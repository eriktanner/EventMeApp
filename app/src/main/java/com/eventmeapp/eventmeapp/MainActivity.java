package com.eventmeapp.eventmeapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private PopupWindow pw;
    ImageView settingsButton, markerButtonHeader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar_mainactivity);
        setContentView(R.layout.activity_main);

        /*Tab Manager*/
        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        settingsButton = (ImageView) findViewById(R.id.iv_settings);
        markerButtonHeader = (ImageView) findViewById(R.id.iv_markerForHeader);



        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(MainActivity.this, Settings.class);
                MainActivity.this.startActivity(mainIntent);
            }
        });
        markerButtonHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                initiatePopupWindow(v);
            }
        });

        /*markerButtonHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(MainActivity.this, MarkerGuide.class);
                MainActivity.this.startActivity(mainIntent);
            }
        });
*/

        //Sets Icons For Tabs
        View view1 = getLayoutInflater().inflate(R.layout.customtab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_pink_map);
        tabLayout.getTabAt(0).setCustomView(view1);
        View view2 = getLayoutInflater().inflate(R.layout.customtab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_pink_idea);
        tabLayout.getTabAt(1).setCustomView(view2);
        View view3 = getLayoutInflater().inflate(R.layout.customtab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_pink_user);
        tabLayout.getTabAt(2).setCustomView(view3);

        /*Listener for Tabs*/
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

    }



    /*Class used in conjunction with Tab Manager*/
    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragmentNames[] = {"TabMap", "Suggested", "Events"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TabMap();
                case 1:
                    return new TabSuggestions();
                case 2:
                    return new TabProfile();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragmentNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    private void initiatePopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) MainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.marker_tutotial,
                    (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 900, 900, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER, 0, 0);

            /*TextView mResultText = (TextView) layout.findViewById(R.id.server_status_text);
            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
