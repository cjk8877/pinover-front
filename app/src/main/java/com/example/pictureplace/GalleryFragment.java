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
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {
    View layout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GalleryFragment() {
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
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
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
        layout = inflater.inflate(R.layout.fragment_gallery, container, false);
        // Inflate the layout for this fragment

        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> srcList = new ArrayList<>();
        list.add("오늘");
        for (int i =0; i<9; i++){
            list.add(String.format("%d일 전", i+1));
            srcList.add("https://www.picplace.kro.kr/posting/picture/34229dc30066cc4bed3fab82adad2afd");
            srcList.add("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FIH7pg%2FbtqGH1ZgBXx%2Ft8KhtXCPoKDM1ZKljOv270%2Fimg.jpg");
            srcList.add("https://ppss.kr/wp-content/uploads/2014/04/007-549x377.jpg");
            srcList.add("https://ppss.kr/wp-content/uploads/2014/04/007-549x377.jpg");
        }

        RecyclerView recyclerView = layout.findViewById(R.id.galleryRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        GalleryRcyAdapter adapter = new GalleryRcyAdapter(list, srcList);
        recyclerView.setAdapter(adapter);
        return layout;
    }
}