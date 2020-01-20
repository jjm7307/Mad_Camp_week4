package com.madcamp.week4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import com.madcamp.week4.adapters.ViewPagerAdapter;
import com.madcamp.week4.fragments.FragmentCoin;
import com.madcamp.week4.fragments.FragmentMap;
import com.madcamp.week4.fragments.fragments_test;

public class MainActivity extends AppCompatActivity {
    String[] PERMISSIONS = {
            //List for get permission
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public FragmentCoin fragmentA = new FragmentCoin();
    public FragmentMap fragmentB = new FragmentMap();
    public fragments_test fragmentC = new fragments_test();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        askPermission();
        while(!hasPermissions(this,PERMISSIONS)){}


        setContentView(R.layout.activity_main);

        Intent intent = new Intent();

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(fragmentA, "wallet");
        adapter.addFragment(fragmentB, "map");
        adapter.addFragment(fragmentC, "camera");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });
    }

    private void changeView(int index) {
        switch (index) {
            case 0:
                viewPager.setCurrentItem(0);
                break;
            case 1:
                viewPager.setCurrentItem(1);
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                break;
            case 2:
                viewPager.setCurrentItem(2);
                Intent intent2 = new Intent(MainActivity.this, UnityPlayerActivity.class);
                startActivity(intent2);
                break;
        }
    }
    private void askPermission() {
        int PERMISSION_ALL = 1;

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
