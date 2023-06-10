package com.example.pictureplace;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment# newInstance} factory method to
 * create an instance of this fragment.
 */

public class MapFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener, MainActivity.OnBackPressedListener {
    private MapView mapView = null;
    MainActivity mainActivity;
    LinearLayout pinInfo;
    MainActivity contextMain;
    ImageView bottomArrow;
    LocationManager manager;
    GPSListener gpsListener;
    GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    Location location = null;
    View layout;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    boolean isPinInfoOpened;
    Marker myMarker;
    MarkerOptions myLocationMarker;
    Circle circle;
    CircleOptions circle1KM;
    RetrofitFactory retrofitFactory = new RetrofitFactory();
    Retrofit retrofit;
    ArrayList<LatLng> pinLocation;
    Set<LatLng> addedLocations;
    Call<List<MapPinsDTO>> call;
    ILoginService service;
    List<Marker> markers;
    ArrayList<String> placeId;
    TextView pinName, pinRecommendCount, pinComment1, pinComment2;
    ImageView pinImage1, pinImage2, pinImage3;
    Button pinGatheringBtn;
    String markerId;

    @Override
    public void onBackPressed(){
        if(pinInfo.getVisibility() == View.VISIBLE) {
            pinInfo.setVisibility(View.GONE);
            contextMain.visibleBottomMenu();
        }
    }

    public boolean isPinInfoOpened(){
        return isPinInfoOpened;
    };

    public MapFragment() {
        // required
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_map, container, false);
        contextMain = (MainActivity) MainActivity.contextMain;

        //location tracing setting
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        gpsListener = new GPSListener();

        mapView = (MapView) layout.findViewById(R.id.map);
        mapView.getMapAsync(this);

        //id connecting
        pinInfo = (LinearLayout) layout.findViewById(R.id.pinInfo);
        bottomArrow = (ImageView) layout.findViewById(R.id.pinInfoBottomArrow);
        pinName = layout.findViewById(R.id.pinName);
        pinRecommendCount = layout.findViewById(R.id.recommendCount);
        pinComment1 = layout.findViewById(R.id.userComment1);
        pinComment2 = layout.findViewById(R.id.userComment2);
        pinImage1 = layout.findViewById(R.id.pinImage1);
        pinImage2 = layout.findViewById(R.id.pinImage2);
        pinImage3 = layout.findViewById(R.id.pinImage3);
        pinGatheringBtn = layout.findViewById(R.id.pinGatheringBtn);

        int hasFinePer = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        pinGatheringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapPinGathering.class);
                intent.putExtra("placeId", markerId);
                startActivity(intent);
            }
        });

        bottomArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinInfo.setVisibility(View.GONE);
                contextMain.visibleBottomMenu();
                isPinInfoOpened = false;
            }
        });
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, gpsListener);
                //manager.removeUpdates(gpsListener);
            } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, gpsListener);
                //manager.removeUpdates(gpsListener);
            }
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
            Log.i("MyLocTest", "onResume에서 requestLocationUpdates() 되었습니다.");
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(layout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }


        // GPS provider를 이용전에 퍼미션 체크
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "접근 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {

        }

        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(false);
            }
        }
        Log.i("MyLocTest", "onPause에서 removeUpdates() 되었습니다.");

        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addedLocations = new HashSet<>();
        retrofit = retrofitFactory.newRetrofit();
        service = retrofit.create(ILoginService.class);
        call = service.getMapPins((float)36.367339, (float)127.384896, (float) 15.0);
        markers = new ArrayList<>();
        pinLocation = new ArrayList<>();
        placeId = new ArrayList<>();

        call.enqueue(new Callback<List<MapPinsDTO>>() {
            @Override
            public void onResponse(Call<List<MapPinsDTO>> call, Response<List<MapPinsDTO>> response) {
                MapPinsDTO mapPinsDTO;
                for(int i = 0; i < response.body().size(); i++){
                    mapPinsDTO = response.body().get(i);
                    pinLocation.add(new LatLng(Double.parseDouble(mapPinsDTO.getLatitude()), Double.parseDouble(mapPinsDTO.getLongitude())));
                    placeId.add(mapPinsDTO.getLocationId());
                }

                for(LatLng location : pinLocation){
                    if(!addedLocations.contains(location)){
                        MarkerOptions markerOptions = new MarkerOptions().position(location);
                        Marker marker = googleMap.addMarker(markerOptions);
                        markers.add(marker);
                        addedLocations.add(location);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MapPinsDTO>> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });



        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("수도");
        googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        showCurrentLocation(latitude, longitude);

        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnCameraIdleListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker maker) {
        Call<List<MyPinDTO>> markerInfoCall = service.postLoad(maker.getTitle());

        markerInfoCall.enqueue(new Callback<List<MyPinDTO>>() {
            @Override
            public void onResponse(Call<List<MyPinDTO>> call, Response<List<MyPinDTO>> response) {
                int recommendCount = 0;
                if (response.isSuccessful() && response.body() != null) {
                    List<MyPinDTO> myPin = response.body();
                    pinInfo.setVisibility(View.VISIBLE);
                    contextMain.hideBottomMenu();
                    isPinInfoOpened = true;

                    markerId = maker.getTitle();
                    pinName.setText(myPin.get(0).getLocationname());
                    pinComment1.setText(myPin.get(0).getContent());
                    pinComment2.setText(myPin.get(1).getContent());

                    //TODO :매우 불안정 수정 요망
                    Glide.with(getActivity()).load(myPin.get(0).getPictures().get(0)).centerCrop().into(pinImage1);
                    Glide.with(getActivity()).load(myPin.get(1).getPictures().get(0)).centerCrop().into(pinImage2);
                    Glide.with(getActivity()).load(myPin.get(2).getPictures().get(0)).centerCrop().into(pinImage3);

                    for(int i = 0; i < myPin.size(); i++){
                        recommendCount += myPin.get(i).getRecommendCount();
                    }

                    pinRecommendCount.setText("+" + recommendCount);

                }else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(getActivity(), "에러가 발생했습니다. 조회에 실패했습니다." +
                                "에러 메세지 = " + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MyPinDTO>> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());

            }
        });

        return true;
    }

    // 카메라 움직임을 멈췄을 때 호출됨
    @Override
    public void onCameraIdle() {
        Log.d("camera position", mMap.getCameraPosition().toString());
        //TODO : lat, log, 확대비율로 location/nearest에 위치 정보 GET 요청,

        call = service.getMapPins((float)mMap.getCameraPosition().target.latitude, (float)mMap.getCameraPosition().target.longitude, (float)mMap.getCameraPosition().zoom);

        call.enqueue(new Callback<List<MapPinsDTO>>() {
            @Override
            public void onResponse(Call<List<MapPinsDTO>> call, Response<List<MapPinsDTO>> response) {
                MapPinsDTO mapPinsDTO;
                for(int i = 0; i < response.body().size(); i++){
                    mapPinsDTO = response.body().get(i);
                    pinLocation.add(new LatLng(Double.parseDouble(mapPinsDTO.getLatitude()), Double.parseDouble(mapPinsDTO.getLongitude())));
                    placeId.add(mapPinsDTO.getLocationId());
                }

                for(int i = 0; i < pinLocation.size(); i++){
                    LatLng location = pinLocation.get(i);
                    if(!addedLocations.contains(location)){
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(location).title(placeId.get(i));
                        Marker marker = mMap.addMarker(markerOptions);
                        markers.add(marker);
                        addedLocations.add(location);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MapPinsDTO>> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });

    }

    private void startLocationUpdates() {
        if (!checkLocationServiceStatus()) {
            Log.d("MapFragment", "startLocationUpdates : call showDialogForLocationServiceSetting");
        }
    }

    public boolean checkLocationServiceStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("위치서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서 위치 서비스가 필요합니다\n 위치 서비스를 사용하시겠습니까?");
    }

    public void startLocationService() {
        try {
            Location location = null;

            long minTime = 0;        // 0초마다 갱신 - 바로바로갱신
            float minDistance = 0;

            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String message = "최근 위치1 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                    Log.d("MapFrag", message);
                    Log.i("MyLocTest", "최근 위치1 호출");
                }

                //위치 요청하기
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
                //manager.removeUpdates(gpsListener);
                Toast.makeText(getActivity(), "내 위치1확인 요청함", Toast.LENGTH_SHORT).show();
                Log.i("MyLocTest", "requestLocationUpdates() 내 위치1에서 호출시작 ~~ ");

            } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String message = "최근 위치2 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                    Log.d("MapFrag", message);
                    //showCurrentLocation(latitude, longitude);

                    Log.i("MyLocTest", "최근 위치2 호출");
                }

                //위치 요청하기
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
                //manager.removeUpdates(gpsListener);
                Toast.makeText(getActivity(), "내 위치2확인 요청함", Toast.LENGTH_SHORT).show();
                Log.i("MyLocTest", "requestLocationUpdates() 내 위치2에서 호출시작 ~~ ");
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    class GPSListener implements LocationListener {
        // 위치 확인되었을때 자동으로 호출됨 (일정시간 and 일정거리)
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double zoomLevel = mMap.getCameraPosition().zoom;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private void showCurrentLocation(double latitude, double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        showMyLocationMarker(curPoint);
    }

    private void showMyLocationMarker(LatLng curPoint) {
        if (myLocationMarker == null) {
            myLocationMarker = new MarkerOptions(); // 마커 객체 생성
            myLocationMarker.position(curPoint);
            myLocationMarker.title("최근위치 \n");
            myLocationMarker.snippet("*GPS로 확인한 최근위치");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource((R.drawable.mylocation)));
            myMarker = mMap.addMarker(myLocationMarker);
        } else {
            myMarker.remove(); // 마커삭제
            myLocationMarker.position(curPoint);
            myMarker = mMap.addMarker(myLocationMarker);
        }

        // 반경추가
        if (circle1KM == null) {
            circle1KM = new CircleOptions().center(curPoint) // 원점
                    .radius(1000)       // 반지름 단위 : m
                    .strokeWidth(1.0f)    // 선너비 0f : 선없음
                    .fillColor(Color.parseColor("#1AFFFFFF")); // 배경색
            circle = mMap.addCircle(circle1KM);


        } else {
            circle.remove(); // 반경삭제
            circle1KM.center(curPoint);
            circle = mMap.addCircle(circle1KM);
        }
    }
}



