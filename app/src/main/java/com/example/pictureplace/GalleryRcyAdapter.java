package com.example.pictureplace;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GalleryRcyAdapter extends RecyclerView.Adapter<GalleryRcyAdapter.ViewHolder> {

    private ArrayList<String> mData = null;
    private ArrayList<String> mImgSrc;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView1, imageView2, imageView3, imageView4;

        ViewHolder(View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.griHeaderTV);
            imageView1 = itemView.findViewById(R.id.galleryIV1);
            imageView2 = itemView.findViewById(R.id.galleryIV2);
            imageView3 = itemView.findViewById(R.id.galleryIV3);
            imageView4 = itemView.findViewById(R.id.galleryIV4);
        }
    }

    GalleryRcyAdapter(ArrayList<String> list, ArrayList<String> imgSrc){
        mData = list;
        mImgSrc = imgSrc;
    }

    @Override
    public GalleryRcyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.gallery_recyclerview_item, parent, false);
        GalleryRcyAdapter.ViewHolder vh = new GalleryRcyAdapter.ViewHolder(view);
        return vh;
     }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = mData.get(position);
        holder.textView.setText(text);
        Log.d("GALLERY", Integer.toString(mData.size()-1));
        if(position < mData.size()-1) {
            Glide.with(context).load(mImgSrc.get(position * 4)).centerCrop().into(holder.imageView1);
            Glide.with(context).load(mImgSrc.get(position * 4 + 1)).centerCrop().into(holder.imageView2);
            Glide.with(context).load(mImgSrc.get(position * 4 + 2)).centerCrop().into(holder.imageView3);
            Glide.with(context).load(mImgSrc.get(position * 4 + 3)).centerCrop().into(holder.imageView4);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
