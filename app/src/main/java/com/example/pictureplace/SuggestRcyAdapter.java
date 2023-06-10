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
    private ArrayList<String> mImgSrc;
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

    SuggestRcyAdapter(ArrayList<String> list, ArrayList<String> imgSrc){
        mData = list;
        mImgSrc = imgSrc;
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

        if(position < mData.size() && mImgSrc.size() > position * 4 +3) {
            Glide.with(context).load(mImgSrc.get(position * 4)).centerCrop().into(holder.imageView1);
            Glide.with(context).load(mImgSrc.get(position * 4 + 1)).centerCrop().into(holder.imageView2);
            Glide.with(context).load(mImgSrc.get(position * 4 + 2)).centerCrop().into(holder.imageView3);
            Glide.with(context).load(mImgSrc.get(position * 4 + 3)).centerCrop().into(holder.imageView4);
        }

//        int j = 0;
//        for(int i = 0 ; position * 4 + i < (position+1) * 4; i++, j++) {
//               Glide.with(context).load(mImgSrc.get(position * 4 + i)).centerCrop().into(holder.imageViews[j%4]);
//        }

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
