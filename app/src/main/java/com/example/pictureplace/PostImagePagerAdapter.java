package com.example.pictureplace;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PostImagePagerAdapter extends RecyclerView.Adapter<PostImagePagerAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<String> imageSrc;

    public PostImagePagerAdapter(Context context, ArrayList<String> imageSrc) {
        this.context = context;
        this.imageSrc = imageSrc;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String src = imageSrc.get(position);
        Glide.with(context).load(imageSrc.get(position)).centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageSrc.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.regiImageView);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }
}