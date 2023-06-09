package com.example.pictureplace;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinActivity extends Activity {
    EditText edtId, edtPass, edtRepass, edtNickname, edtAddress, edtPN;
    Button confirmBtn;
    private Retrofit retrofit;
    RetrofitFactory retrofitFactory= new RetrofitFactory();
    private final String TAG = "JoinActvity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        retrofit = retrofitFactory.newRetrofit();

        edtId = findViewById(R.id.edtJoinId);
        edtPass = findViewById(R.id.edtJoinPass);
        edtRepass = findViewById(R.id.edtJoinPassChk);
        edtNickname = findViewById(R.id.edtJoinNickname);
        edtAddress = findViewById(R.id.edtJoinAddress);
        edtPN = findViewById(R.id.edtJoinPN);
        confirmBtn = findViewById(R.id.btnJoin);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //예외처리 진행
                if (!edtPass.getText().toString().equals(edtRepass.getText().toString()))
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG);
                else if (edtId.getText().toString().contains(" "))
                    Toast.makeText(getApplicationContext(), "아이디에 공백문자를 포함시킬수 없습니다.", Toast.LENGTH_LONG);
                else if (edtPass.getText().toString().contains(" "))
                    Toast.makeText(getApplicationContext(), "비밀번호에 공백문자를 포함시킬 수 없습니다.", Toast.LENGTH_LONG);
                else if(edtNickname.equals(""))
                    Toast.makeText(getApplicationContext(),"이름이 비어있습니다.", Toast.LENGTH_LONG).show();
                else
                    join();
            }
        });
    }

    private void join(){
        String id = edtId.getText().toString();
        String pass = edtPass.getText().toString();
        String nickname = edtNickname.getText().toString();
        String address = edtAddress.getText().toString();
        String hp = edtPN.getText().toString();

        ILoginService service = retrofit.create(ILoginService.class);
        Call<String> call = service.joinMember(id, pass, nickname, address, hp);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                {
                    Log.e("onSuccess", response.body());
                    Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });
    }


}