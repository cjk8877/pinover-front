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
                .baseUrl(ILoginService.loginBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ILoginService service = retrofit.create(ILoginService.class);
        Call<LoginResponse> call = service.getMember(id, pw);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    Log.e("onSuccess", response.body().token);
                    String jsonResponse = response.body().toString();
                    try
                    {
                        parseRegData(jsonResponse);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }else{
                    Log.d("succ_test", Boolean.toString(response.isSuccessful()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });

    }

    public static Context getMContext(){
        return mContext;
    }

    private void parseRegData(String response) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(response);
        Log.d("TEST", response);
        if (jsonObject.optString("status").equals("success"))
        {
            Toast.makeText(getApplicationContext(), "로그인 성공 상태는 " + jsonObject.optString("status"), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d("msg : ", jsonObject.getString("message"));
            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }
}


