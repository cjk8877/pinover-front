package com.example.pictureplace;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RegiLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView locationMap;
    GoogleMap mMap;
    Button btnRegi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMap);
        mapFragment.getMapAsync(this);

        btnRegi = findViewById(R.id.btnRegiLocation);
        btnRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng centerLatLng = mMap.getCameraPosition().target;

                // 좌표를 RegiPinActivity로 전달
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", centerLatLng.latitude);
                resultIntent.putExtra("longitude", centerLatLng.longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap){
        mMap = googleMap;
        enableMyLocation();

        LatLng SEOUL = new LatLng(36.367339, 127.384896);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("대전");
        markerOptions.snippet("수도");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    private void enableMyLocation() {
        // 위치 권한을 확인합니다.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 권한이 이미 허용된 경우 현재 위치를 활성화합니다.
            mMap.setMyLocationEnabled(true);
        } else {
            // 권한이 허용되지 않은 경우 권한 요청을 수행합니다.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
}
