package com.example.pictureplace;

import android.content.Context;
import android.media.Image;
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

    GalleryRcyAdapter(ArrayList<String> list){
        mData = list;
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
        Glide.with(context).load("https://mblogthumb-phinf.pstatic.net/MjAyMDA4MDNfMjM4/MDAxNTk2NDE5NDA0NTYw.31arD1NtcPUJ-afD4xAAdpa_HbDBXrrR0TxRF6IdI3og.nXMzZFUBfHe0gHCpsQCf07ZFzaO9ToAUTY3xKyxrmJ4g.JPEG.csprint1/%EB%8B%A4%EC%96%91%ED%95%9C%EC%B1%85.jpg?type=w800").centerCrop().into(holder.imageView1);
        Glide.with(context).load("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FIH7pg%2FbtqGH1ZgBXx%2Ft8KhtXCPoKDM1ZKljOv270%2Fimg.jpg").centerCrop().into(holder.imageView2);
        Glide.with(context).load("https://ppss.kr/wp-content/uploads/2014/04/007-549x377.jpg").centerCrop().into(holder.imageView3);
        Glide.with(context).load("https://gimg.gilbut.co.kr/book/BN003653/rn_view_BN003653.jpg").centerCrop().into(holder.imageView4);
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
