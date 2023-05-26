package com.example.pictureplace;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class LoadingActivity extends Activity {
    ImageView decoIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        decoIV = findViewById(R.id.flipDecoIV);
        decoIV.setImageBitmap(rotateImage(BitmapFactory.decodeResource(getResources(), R.drawable.login_deco)));

        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }


    //flip image
    public Bitmap rotateImage(Bitmap src) {
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }
}

