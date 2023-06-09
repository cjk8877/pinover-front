package com.example.pictureplace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPinFragment extends Fragment {
    View layout;

    ArrayList<ArrayList<String>> mImageSrcs;
    ArrayList<String> myImageSrcs;
    ArrayList<String> mUserName;
    ArrayList<String> mDate;
    ArrayList<String> mComment;
    ArrayList<Integer> mLikeCount;
    ArrayList<String> mContent;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyPinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPinFragment newInstance(String param1, String param2) {
        MyPinFragment fragment = new MyPinFragment();
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
        layout = inflater.inflate(R.layout.fragment_my_pin, container, false);

        RecyclerView postRecyclerView = layout.findViewById(R.id.myPinRecycler);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myImageSrcs = new ArrayList<String>();
        mImageSrcs = new ArrayList<ArrayList<String>>();
        mUserName = new ArrayList<String>();
        mDate = new ArrayList<String>();
        mComment = new ArrayList<String>();
        mLikeCount = new ArrayList<Integer>();
        mContent = new ArrayList<String>();

        myImageSrcs.add("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FIH7pg%2FbtqGH1ZgBXx%2Ft8KhtXCPoKDM1ZKljOv270%2Fimg.jpg");
        myImageSrcs.add("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FIH7pg%2FbtqGH1ZgBXx%2Ft8KhtXCPoKDM1ZKljOv270%2Fimg.jpg");


        //dummy data
        for(int i = 0; i < 5; i++){
            mImageSrcs.add(myImageSrcs);
            mUserName.add("윤대웅");
            mDate.add("23.02.08");
            mComment.add("좋네요");
            mLikeCount.add(18);
            mContent.add("이번에 바뀌었다고 해서 가보았어요.");
        }

        PostRcyAdapter adapter = new PostRcyAdapter(mImageSrcs, mUserName, mDate, mComment, mLikeCount, mContent);
        postRecyclerView.setAdapter(adapter);

        return layout;
    }
}