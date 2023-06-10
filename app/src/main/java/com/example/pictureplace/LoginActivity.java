package com.example.pictureplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Cookie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity  {
    TextView tvJoin;
    Button loginBtn;
    EditText editTextLoginID, editTextLoginPW;
    private Retrofit retrofit;
    private final String TAG = "LoginActivity";
    ImageView loginDecoIv;
    private static Context mContext;
    TokenManager tokenManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvJoin = (TextView) findViewById(R.id.tvJoin);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        editTextLoginID = (EditText) findViewById(R.id.edtLoginID);
        editTextLoginPW = (EditText) findViewById(R.id.edtLoginPW);

        //deco Setting
        loginDecoIv = findViewById(R.id.loginDecoIv);
        loginDecoIv.setColorFilter(Color.parseColor("#5DB0E7"));

        LoginActivity.mContext = getApplicationContext();

        //토큰매니저 설정
        tokenManager = new TokenManager(getApplicationContext());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextLoginID.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"아이디가 비어있습니다.", Toast.LENGTH_LONG).show();
                else if(editTextLoginPW.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"비밀번호가 비어있습니다..", Toast.LENGTH_LONG).show();
                else
                    loginAction();
            }
        });

        tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent joinIntent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(joinIntent);
            }
        });
    }

    public void loginAction(){
        String id = editTextLoginID.getText().toString().trim();
        String pw = editTextLoginPW.getText().toString().trim();


        //retrofit = RetrofitFactory.createRetrofit(this, ILoginService.loginBaseUrl);
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(ILoginService.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ILoginService service = retrofit.create(ILoginService.class);
        Call<LoginDTO> call = service.getMember(id, pw);

        call.enqueue(new Callback<LoginDTO>() {
            @Override
            public void onResponse(@NonNull Call<LoginDTO> call, @NonNull Response<LoginDTO> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    List<String> Cookielist = response.headers().values("Set-Cookie");
                    String accessToken = "Bearer " + (Cookielist.get(0).split(";"))[0].split("=")[1];
                    tokenManager.saveToken(accessToken);
                    String jsonResponse = response.body().toString();
                    Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginDTO> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });

    }

    public static Context getMContext(){
        return mContext;
    }

}


