package com.example.pictureplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity{
    private final String TAG = "MainACTIVITY";
    Toolbar toolbar;
    BottomNavigationView navigationView;
    private DrawerLayout drawerLayout;
    private LinearLayout drawerView, drawerHeader;
    private OnBackPressedListener mapBackPressFragment;
    MapFragment mapFragment;
    MyPinFragment myPinFragment;
    SuggestFragment suggestFragment;
    LocationPinsFragment locationPinsFragment;
    Intent loadIntent, regiIntent;
    public static Context contextMain;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //context Control setting
        contextMain = this;

        //load page
        loadIntent = new Intent(this, LoadingActivity.class);
        startActivity(loadIntent);

        //regiIntent setting
        regiIntent = new Intent(this, RegiPinActivity.class);

        //drawer setting
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (LinearLayout)findViewById(R.id.drawerView);
        drawerHeader = (LinearLayout)findViewById(R.id.drawer_header);
        drawerLayout.addDrawerListener(listener);
        ImageView toolbarMenu;
        toolbarMenu = (ImageView) findViewById(R.id.toolbarMenu);

        //fragment setting
        mapFragment = new MapFragment();
        myPinFragment = new MyPinFragment();
        suggestFragment = new SuggestFragment();
        locationPinsFragment = new LocationPinsFragment();

        //toolbar manu onclick
        toolbarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        //in drawer Button onclick
        drawerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        setCurrentFragment(mapFragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.homeFg, mapFragment).commit();

        navigationView = findViewById(R.id.bottomMenu);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tabMap:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFg, mapFragment).commit();
                        return true;
                    case R.id.tabSearch:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFg, suggestFragment).commit();
                        return true;
                    case R.id.tabGallery:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFg, locationPinsFragment).commit();
                        return true;
                    case R.id.tabMyPin:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFg, myPinFragment).commit();
                        return true;
                    case R.id.tabAddMarker:
                        //게시글 올리기 액티비티 실행
                        startActivity(regiIntent);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.d(TAG, Boolean.toString(mapFragment.isPinInfoOpened()));
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Log.d(TAG, "드로어가 열려있습니다");
            drawer.closeDrawer(GravityCompat.START);
        } else if(mapBackPressFragment != null && mapFragment.isPinInfoOpened()){
            mapBackPressFragment.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }

    //toolbar action manage
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    public void hideBottomMenu(){
        navigationView.setVisibility(View.INVISIBLE);
    }

    public void visibleBottomMenu(){
        navigationView.setVisibility(View.VISIBLE);
    }

    public LocationManager getLocationSystemService(){
        return (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }



    public interface OnBackPressedListener{
        void onBackPressed();
    }

    public void setCurrentFragment(OnBackPressedListener fragment){
        mapBackPressFragment = fragment;
    }
}