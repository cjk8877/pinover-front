package com.example.pictureplace;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuggestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuggestFragment extends Fragment{
    View layout;
    RetrofitFactory retrofitFactory = new RetrofitFactory();
    Retrofit retrofit;

    ArrayList<String> srcList;
    ArrayList<String> list;
    ArrayList<String> weeklySrcs, popularSrcs, randomSrcs;
    private final int WEEKLY_LOCA = 1;
    private final int POPULAR = 2;
    private final int RANDOM = 3;
    int responseCounter = 0;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SuggestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuggestFragment newInstance(String param1, String param2) {
        SuggestFragment fragment = new SuggestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_search, container, false);

        retrofit = retrofitFactory.newRetrofit();
        ILoginService service = retrofit.create(ILoginService.class);
        Call<List<SuggestDTO>> call = service.getSuggestWeekly();

        list = new ArrayList<>();
        srcList = new ArrayList<>();

        weeklySrcs = new ArrayList<>();
        popularSrcs = new ArrayList<>();
        randomSrcs = new ArrayList<>();

        //추천 이름 정리
        list.add("최근에 가장 핀이 많이 올라온 곳들");
        list.add("일주일간 가장 추천을 많이 받은 곳들");
        list.add("랜덤한 유저들의 핀 보여주기");

        //최근에 많이 올라온 핀 목록
        call.enqueue(new Callback<List<SuggestDTO>>() {
            @Override
            public void onResponse(Call<List<SuggestDTO>> call, Response<List<SuggestDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SuggestDTO suggestDTO;

                    for (int i = 0 ; i < response.body().size(); i++) {
                        suggestDTO = response.body().get(i);

                        if(suggestDTO.getPictures() != null) {
                            for (int j = 0; j < suggestDTO.getPictures().size() && weeklySrcs.size() < 4; j++) {
                                weeklySrcs.add(suggestDTO.getPictures().get(j));
                            }
                        }
                    }
                    responseCounter++;
                    checkResponseComplete();
                } else {
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
            public void onFailure(Call<List<SuggestDTO>> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });

        call = service.getSuggestPopular();

        //추천을 많이 받은 목록
        call.enqueue(new Callback<List<SuggestDTO>>() {
            @Override
            public void onResponse(Call<List<SuggestDTO>> call, Response<List<SuggestDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SuggestDTO suggestDTO;

                    for (int i = 0 ; i < response.body().size(); i++) {
                        suggestDTO = response.body().get(i);

                        if(suggestDTO.getPictures() != null) {
                            for (int j = 0; j < suggestDTO.getPictures().size() && popularSrcs.size() < 4; j++) {
                                popularSrcs.add(suggestDTO.getPictures().get(j));
                            }
                        }
                    }
                    responseCounter++;
                    checkResponseComplete();
                } else {
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
            public void onFailure(Call<List<SuggestDTO>> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });

        call = service.getSuggestRandom();
        //랜덤 목록
        call.enqueue(new Callback<List<SuggestDTO>>() {
            @Override
            public void onResponse(Call<List<SuggestDTO>> call, Response<List<SuggestDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SuggestDTO suggestDTO;

                    for (int i = 0 ; i < response.body().size(); i++) {
                        suggestDTO = response.body().get(i);

                        if(suggestDTO.getPictures() != null) {
                            for (int j = 0; j < suggestDTO.getPictures().size() && randomSrcs.size() < 4; j++) {
                                randomSrcs.add(suggestDTO.getPictures().get(j));
                            }
                        }
                    }

                    responseCounter++;
                    checkResponseComplete();
                } else {
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
            public void onFailure(Call<List<SuggestDTO>> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });

        return layout;
    }

    private void checkResponseComplete(){
        if(responseCounter == 3){
            for(int i = 0; i < weeklySrcs.size(); i++){
                srcList.add(weeklySrcs.get(i));
            }
            for(int i = 0; i < popularSrcs.size(); i++){
                srcList.add(popularSrcs.get(i));
            }
            for(int i = 0; i < randomSrcs.size(); i++){
                srcList.add(randomSrcs.get(i));
            }

            RecyclerView recyclerView = layout.findViewById(R.id.galleryRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            SuggestRcyAdapter adapter = new SuggestRcyAdapter(list, srcList);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new SuggestRcyAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent suggestIntent = new Intent(getActivity(), SuggestActivity.class);
                    suggestIntent.putExtra("suggestSubject", position + 1);
                    startActivity(suggestIntent);
                }
            });
        }
    }
}