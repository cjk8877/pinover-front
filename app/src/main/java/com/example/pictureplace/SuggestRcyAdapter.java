package com.example.pictureplace;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SuggestRcyAdapter extends RecyclerView.Adapter<SuggestRcyAdapter.ViewHolder> {

    private ArrayList<String> mData = null;
    private ArrayList<ArrayList<String>> mImgSrcs;
    Context context;
    Intent intent;
    private OnItemClickListener mListener;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView1, imageView2, imageView3, imageView4;
        ImageView[] imageViews;
        Button suggestItemBtn;

        ViewHolder(View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.griHeaderTV);
            imageView1 = itemView.findViewById(R.id.galleryIV1);
            imageView2 = itemView.findViewById(R.id.galleryIV2);
            imageView3 = itemView.findViewById(R.id.galleryIV3);
            imageView4 = itemView.findViewById(R.id.galleryIV4);
            suggestItemBtn = itemView.findViewById(R.id.suggestItemBtn);

            imageViews = new ImageView[]{imageView1, imageView2, imageView3, imageView4};
        }
    }

    SuggestRcyAdapter(ArrayList<String> list, ArrayList<ArrayList<String>> imgSrcs){
        mData = list;
        mImgSrcs = imgSrcs;
    }

    @Override
    public SuggestRcyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.suggest_recyclerview_item, parent, false);
        SuggestRcyAdapter.ViewHolder vh = new SuggestRcyAdapter.ViewHolder(view);
        return vh;
     }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = mData.get(position);
        holder.textView.setText(text);
        Log.d("GALLERY", Integer.toString(mData.size()-1));

        if(mImgSrcs.get(position).size() >= 4) {
            for (int i = 0; i < 4; i++) {
                Glide.with(context).load(mImgSrcs.get(position).get(i)).centerCrop().into(holder.imageViews[i]);
            }
        }else{
            for (int i = 0; i < mImgSrcs.get(position).size(); i++) {
                Glide.with(context).load(mImgSrcs.get(position).get(i)).centerCrop().into(holder.imageViews[i]);
            }
            for (int i = mImgSrcs.get(position).size(); i < 4; i++) {
                Glide.with(context).load("https://www.alpha.co.kr/common/img/noimage/268.png").centerCrop().into(holder.imageViews[i]);
            }
        }

        holder.suggestItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
