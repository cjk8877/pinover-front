package com.example.pictureplace;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
 * Use the {@link LocationPinsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationPinsFragment extends Fragment {
    View layout;

    ArrayList<ArrayList<String>> mImageSrcs, mTags;
    ArrayList<String> myImageSrcs, mUserName, mDate, mComment, mContent, myTags;
    ArrayList<Integer> mLikeCount;
    Retrofit retrofit;
    RetrofitFactory retrofitFactory = new RetrofitFactory();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LocationPinsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationPinsFragment newInstance(String param1, String param2) {
        LocationPinsFragment fragment = new LocationPinsFragment();
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
        layout = inflater.inflate(R.layout.fragment_location_pins, container, false);

        RecyclerView postRecyclerView = layout.findViewById(R.id.myPinRecycler);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mImageSrcs = new ArrayList<ArrayList<String>>();
        mUserName = new ArrayList<String>();
        mDate = new ArrayList<String>();
        mComment = new ArrayList<String>();
        mLikeCount = new ArrayList<Integer>();
        mContent = new ArrayList<String>();
        mTags = new ArrayList<ArrayList<String>>();

        retrofit = retrofitFactory.newRetrofit();
        ILoginService service = retrofit.create(ILoginService.class);
        TokenManager tokenManager = new TokenManager(getActivity());
        Call<List<MyPinDTO>> call = service.myPin(tokenManager.getToken());

        call.enqueue(new Callback<List<MyPinDTO>>() {
            @Override
            public void onResponse(Call<List<MyPinDTO>> call, Response<List<MyPinDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MyPinDTO myPinDTO;

                    for (int i = 0 ; i < response.body().size(); i++) {
                        myImageSrcs = new ArrayList<String>();
                        myTags = new ArrayList<String>();
                        myPinDTO = response.body().get(i);

                        if(myPinDTO.getPictures() != null) {
                            for (int j = 0; j < myPinDTO.getPictures().size(); j++) {
                                myImageSrcs.add(myPinDTO.getPictures().get(j));
                                String mypic = myPinDTO.getPictures().get(j);
                            }
                        }

                        if(response.body().get(i).getTags() != null) {
                            for (int j = 0; j < myPinDTO.getTags().size(); j++) {
                                myTags.add(myPinDTO.getTags().get(j));
                            }
                        }else
                            myTags.add("태그가 없습니다.");

                        mTags.add(myTags);
                        mImageSrcs.add(myImageSrcs);
                        mDate.add(myPinDTO.getPostDate());
                        mUserName.add(myPinDTO.getUserId());
                        mLikeCount.add(myPinDTO.getRecommendCount());
                        mContent.add(myPinDTO.getContent());
                        mComment.add("좋네요");
                    }

                    PostRcyAdapter adapter = new PostRcyAdapter(mImageSrcs, mUserName, mDate, mComment, mLikeCount, mContent, mTags);
                    postRecyclerView.setAdapter(adapter);

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
            public void onFailure(Call<List<MyPinDTO>> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });

        return layout;
    }
}