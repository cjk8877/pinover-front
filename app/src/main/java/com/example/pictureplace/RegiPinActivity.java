package com.example.pictureplace;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Object.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegiPinActivity extends AppCompatActivity implements ImagePagerAdapter.OnItemClickListener {
    ImageView regiCancelIV;
    Dialog cameraDlg;
    Button regiPinCamera, regiPinGallery, regiPinBtn;
    Intent regiImageIntent;
    ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
    View disclosureCircle;
    Drawable lastImage;
    Bitmap iBitmap;
    EditText regiContentEdt;
    LinearLayout locationRegi, disclosure;
    TextView regiLocationTV, disclosureTV;
    Retrofit retrofit;
    RetrofitFactory retrofitFactory;
    String mPlaceId;
    GeoApiContext geoApiContext;

    List<File> imageFiles = new ArrayList<>();
    List<MultipartBody.Part> imageParts = new ArrayList<>();
    List<String> tagsArrayList = new ArrayList<>();

    private TextureView textureView;
    private ViewPager2 viewPager;
    private ImagePagerAdapter pagerAdapter;

    private final String TAG = "RegiPin";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2;
    private final int REQUEST_LOCATION = 3;
    int disclosureFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi_pin);

        //dialog setting
        cameraDlg = new Dialog(RegiPinActivity.this);
        cameraDlg.setContentView(R.layout.cameradlg);

        //id connecting
        regiCancelIV = findViewById(R.id.regiCancelIV);
        viewPager = findViewById(R.id.viewPager);
        locationRegi = findViewById(R.id.regiLocation);
        regiLocationTV = findViewById(R.id.regiLocationTV);
        disclosure = findViewById(R.id.regiDisclosure);
        disclosureCircle = findViewById(R.id.regiDisclosureCircle);
        disclosureTV = findViewById(R.id.regiDisclosureTV);
        regiPinBtn = findViewById(R.id.regiPinBtn);
        regiContentEdt = findViewById(R.id.regiContentEdt);

        //retrofit setting
        retrofit = new Retrofit.Builder()
                .baseUrl(ILoginService.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ILoginService service = retrofit.create(ILoginService.class);

        //TODO: 예외처리 추가해야함
        regiPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (File imageFile : imageFiles) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("photo", imageFile.getName(), requestBody);
                    imageParts.add(imagePart);
                }

                tagsArrayList.add("여행");
                tagsArrayList.add("테스트");

                String[] tagArray = new String[tagsArrayList.size()];
                tagsArrayList.toArray(tagArray);

                Call<String> call = service.upload(
                        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiJiYmIiLCJpYXQiOjE2ODYyMDM4MjcsImV4cCI6MTY4NjM3NjYyN30.f7KTN3yKd_tj3N3DBjI65wUPsF7I7JxX-EHJ-KxV01A",
                        imageParts,
                        mPlaceId,
                        regiContentEdt.getText().toString(),
                        disclosureTV.getText().toString(),
                        tagArray);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getApplicationContext(), "업로드 성공", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        // 업로드 실패 또는 네트워크 오류 등의 처리
                    }
                });
            }
        });

        disclosure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(disclosureFlag == 0) {
                    ColorStateList colorStateList = getResources().getColorStateList(R.color.disclosure);
                    disclosureCircle.setBackgroundTintList(colorStateList);
                    disclosureTV.setText("공개여부 : 비공");
                    disclosureFlag = 1;
                }else{
                    ColorStateList colorStateList = getResources().getColorStateList(R.color.open);
                    disclosureCircle.setBackgroundTintList(colorStateList);
                    disclosureTV.setText("공개여부 : 공개");
                    disclosureFlag = 0;
                }
            }
        });

        locationRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegiPinActivity.this, RegiLocationActivity.class);
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });

        regiCancelIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lastImage = getResources().getDrawable(R.drawable.regi_image);
        iBitmap = drawableToBitmap(lastImage);
        bitmaps.add(iBitmap);

        pagerAdapter = new ImagePagerAdapter(getApplicationContext(), bitmaps);
        pagerAdapter.setOnItemClickListener(this);

        viewPager.setAdapter(pagerAdapter);
    }

    public void showRegiDlg() {
        cameraDlg.show();

        regiPinCamera = cameraDlg.findViewById(R.id.regiPinCamera);
        regiPinGallery = cameraDlg.findViewById(R.id.regiPinGallery);

        regiPinCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegiPinActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegiPinActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });

        regiPinGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegiPinActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegiPinActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_READ_EXTERNAL_STORAGE);}
                else {
                    regiImageIntent = new Intent(Intent.ACTION_PICK);
                    regiImageIntent.setType("image/*");
                    startActivityForResult(regiImageIntent, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Bitmap mBitmap = null;
                    try {
                        mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Bitmap을 ImageView에 설정
                    if (mBitmap != null) {
                        bitmaps.add(mBitmap);
                    }
                    imageFiles.add(new File(getPathFromUri(uri)));
                    refresh();
                }
                break;
            case 2:
                // TODO : IMAGEFILE.ADD 추가하기
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    bitmaps.add(imageBitmap);
                    refresh();
                }
                break;
            case REQUEST_LOCATION:
                if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK) {
                    double latitude = data.getDoubleExtra("latitude", 0.0);
                    double longitude = data.getDoubleExtra("longitude", 0.0);

                    //도로명주소 리턴
                    Geocoder geocoder = new Geocoder(this, Locale.KOREAN);

                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            String roadAddress = address.getAddressLine(0); // 도로명 주소
                            regiLocationTV.setText(roadAddress);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Read external storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 2);
    }

    @Override
    public void onItemClick() {
        showRegiDlg();
    }

    private void refresh(){
        viewPager.setAdapter(null);
        ImagePagerAdapter recycleAdapter = new ImagePagerAdapter(getApplicationContext(), bitmaps);
        recycleAdapter.setOnItemClickListener(this);
        viewPager.setAdapter(recycleAdapter);
        bitmaps.remove(bitmaps.size()-2);
        bitmaps.add(iBitmap);
        cameraDlg.cancel();

    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private String getPathFromUri(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                path = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return path;
    }
}
