package com.example.pictureplace;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
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

public class SuggestActivity extends AppCompatActivity {
    private final int WEEKLY_LOCA = 1;
    private final int POPULAR = 2;
    private final int RANDOM = 3;
    int subject;
    ArrayList<ArrayList<String>> mImageSrcs;
    ArrayList<String> myImageSrcs, mUserName, mDate, mComment, mContent, myTags;
    ArrayList<Integer> mLikeCount;
    ArrayList<ArrayList<String>> mTags;
    RecyclerView suggestRecyclerView;
    TextView suggestInfoTV;

    RetrofitFactory retrofitFactory = new RetrofitFactory();
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        suggestRecyclerView = findViewById(R.id.suggestRecyclerView);
        suggestRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent mIntent = getIntent();
        subject = mIntent.getIntExtra("suggestSubject", 0);
        Log.d("SuggestActivity", Integer.toString(subject));

        suggestInfoTV = findViewById(R.id.suggestInfoTV);

        retrofit = retrofitFactory.newRetrofit();
        ILoginService service = retrofit.create(ILoginService.class);
        Call<List<SuggestDTO>> call = null;

        mImageSrcs = new ArrayList<ArrayList<String>>();
        mUserName = new ArrayList<String>();
        mDate = new ArrayList<String>();
        mComment = new ArrayList<String>();
        mLikeCount = new ArrayList<Integer>();
        mContent = new ArrayList<String>();
        mTags = new ArrayList<ArrayList<String>>();

        switch(subject){
            case WEEKLY_LOCA:
                call = service.getSuggestWeekly();
                suggestInfoTV.setText("최근에 핀이 많은 목록");
                break;
            case POPULAR:
                call = service.getSuggestPopular();
                suggestInfoTV.setText("최근 추천수 많은 목록");
                break;
            case RANDOM:
                call = service.getSuggestRandom();
                suggestInfoTV.setText("랜덤 태그로 이루어진 목록");
                break;
        }

        call.enqueue(new Callback<List<SuggestDTO>>() {
            @Override
            public void onResponse(Call<List<SuggestDTO>> call, Response<List<SuggestDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SuggestDTO suggestDTO;

                    for (int i = 0 ; i < response.body().size(); i++) {
                        myImageSrcs = new ArrayList<String>();
                        myTags = new ArrayList<String>();
                        suggestDTO = response.body().get(i);

                        if(suggestDTO.getPictures() != null) {
                            for (int j = 0; j < suggestDTO.getPictures().size(); j++) {
                                myImageSrcs.add(suggestDTO.getPictures().get(j));
                                String mypic = suggestDTO.getPictures().get(j);
                            }
                        }

                        if(response.body().get(i).getTags() != null) {
                            for (int j = 0; j < suggestDTO.getTags().size(); j++) {
                                myTags.add(suggestDTO.getTags().get(j));
                            }
                        }else
                            myTags.add("태그가 없습니다.");

                        mTags.add(myTags);
                        mImageSrcs.add(myImageSrcs);
                        mDate.add(suggestDTO.getPostDate());
                        mUserName.add(suggestDTO.getUserId());
                        mLikeCount.add(suggestDTO.getRecommendCount());
                        mContent.add(suggestDTO.getContent());
                        mComment.add("좋네요");
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
            public void onFailure(Call<List<SuggestDTO>> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });
    }

    private void setTitle(String tag){
        switch(subject){
            case WEEKLY_LOCA:
                suggestInfoTV.setText("최근에 핀이 많은 목록");
                break;
            case POPULAR:
                suggestInfoTV.setText("최근 추천수 많은 목록");
                break;
            case RANDOM:
                suggestInfoTV.setText("랜덤 태그" + tag + "의 목록");
                break;
        }
    }
}
