package com.example.pictureplace;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapPinGathering extends AppCompatActivity {
    ArrayList<ArrayList<String>> mImageSrcs;
    ArrayList<String> myImageSrcs, mUserName, mDate, mComment, mContent, myTags;
    ArrayList<Integer> mLikeCount;
    ArrayList<ArrayList<String>> mTags;
    RecyclerView suggestRecyclerView;
    TextView suggestInfoTV;

    RetrofitFactory retrofitFactory = new RetrofitFactory();
    Retrofit retrofit;
    Intent mIntent;
    String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        mIntent = getIntent();

        suggestRecyclerView = findViewById(R.id.suggestRecyclerView);
        suggestRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        placeId = mIntent.getStringExtra("placeId");

        suggestInfoTV = findViewById(R.id.suggestInfoTV);

        retrofit = retrofitFactory.newRetrofit();
        ILoginService service = retrofit.create(ILoginService.class);
        Call<List<MyPinDTO>> call = service.postLoad(placeId);

        mImageSrcs = new ArrayList<ArrayList<String>>();
        mUserName = new ArrayList<String>();
        mDate = new ArrayList<String>();
        mComment = new ArrayList<String>();
        mLikeCount = new ArrayList<Integer>();
        mContent = new ArrayList<String>();
        mTags = new ArrayList<ArrayList<String>>();

        call.enqueue(new Callback<List<MyPinDTO>>() {
            @Override
            public void onResponse(Call<List<MyPinDTO>> call, Response<List<MyPinDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MyPinDTO MyPinDTO;

                    for (int i = 0 ; i < response.body().size(); i++) {
                        myImageSrcs = new ArrayList<String>();
                        myTags = new ArrayList<String>();
                        MyPinDTO = response.body().get(i);

                        if(MyPinDTO.getPictures() != null) {
                            for (int j = 0; j < MyPinDTO.getPictures().size(); j++) {
                                myImageSrcs.add(MyPinDTO.getPictures().get(j));
                                String mypic = MyPinDTO.getPictures().get(j);
                            }
                        }

                        if(response.body().get(i).getTags() != null) {
                            for (int j = 0; j < MyPinDTO.getTags().size(); j++) {
                                myTags.add(MyPinDTO.getTags().get(j));
                            }
                        }else
                            myTags.add("태그가 없습니다.");

                        mTags.add(myTags);
                        mImageSrcs.add(myImageSrcs);
                        mDate.add(MyPinDTO.getPostDate());
                        mUserName.add(MyPinDTO.getUserId());
                        mLikeCount.add(MyPinDTO.getRecommendCount());
                        mContent.add(MyPinDTO.getContent());
                        mComment.add("좋네요");
                        suggestInfoTV.setText(MyPinDTO.getLocationname());
                    }
                    PostRcyAdapter adapter = new PostRcyAdapter(mImageSrcs, mUserName, mDate, mComment, mLikeCount, mContent, mTags);
                    suggestRecyclerView.setAdapter(adapter);


                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(getApplicationContext(), "에러가 발생했습니다. 조회에 실패했습니다." +
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
    }
}

