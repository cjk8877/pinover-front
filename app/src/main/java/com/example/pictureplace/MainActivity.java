package com.example.pictureplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity{
    Toolbar toolbar;
    BottomNavigationView navigationView;
    private DrawerLayout drawerLayout;
    private LinearLayout drawerView, drawerHeader;
    MapFragment mapFragment;
    MyPinFragment myPinFragment;
    SearchFragment searchFragment;
    GalleryFragment galleryFragment;
    public static Context contextMain;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;
    String[] REQUIRED_PERMISSION = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //context Control setting
        contextMain = this;

        //load page
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

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
        searchFragment = new SearchFragment();
        galleryFragment = new GalleryFragment();

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
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFg, searchFragment).commit();
                        return true;
                    case R.id.tabGallery:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFg, galleryFragment).commit();
                        return true;
                    case R.id.tabMyPin:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFg, myPinFragment).commit();
                        return true;
                }
                return false;
            }
        });

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
}