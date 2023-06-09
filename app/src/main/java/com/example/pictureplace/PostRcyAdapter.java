package com.example.pictureplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class PostRcyAdapter extends RecyclerView.Adapter<PostRcyAdapter.ViewHolder>{
    private ArrayList<ArrayList<String>> mImageSrcs;
    private ArrayList<String> mUserName;
    private ArrayList<String> mDate;
    private ArrayList<String> mComment;
    private ArrayList<Integer> mLikeCount;
    private ArrayList<String> mContent;
    Context context;
    PostImagePagerAdapter pagerAdapter;

    public PostRcyAdapter(ArrayList<ArrayList<String>> imageSrcs, ArrayList<String> userName, ArrayList<String> date,
    ArrayList<String> comment, ArrayList<Integer> likeCount, ArrayList<String> content){
        mImageSrcs = imageSrcs;
        mUserName = userName;
        mDate = date;
        mComment = comment;
        mLikeCount = likeCount;
        mContent = content;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewPager2 postViewPager;
        TextView userNameTV, dateTV, commentTV, likeCountTV, contentTV;
        ImageView likeIV, postMenu;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postViewPager = itemView.findViewById(R.id.postViewPager);
            userNameTV = itemView.findViewById(R.id.postUserName);
            dateTV = itemView.findViewById(R.id.postDateTV);
            commentTV = itemView.findViewById(R.id.postCommentTV);
            likeCountTV = itemView.findViewById(R.id.likeCount);
            likeIV = itemView.findViewById(R.id.likeIV);
            postMenu = itemView.findViewById(R.id.postMenu);
            contentTV = itemView.findViewById(R.id.postContent);
        }
    }

    @NonNull
    @Override
    public PostRcyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.post_recyclerview_item, parent, false);
        PostRcyAdapter.ViewHolder vh = new PostRcyAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PostRcyAdapter.ViewHolder holder, int position) {
        pagerAdapter = new PostImagePagerAdapter(context.getApplicationContext(), mImageSrcs.get(position));
        holder.postViewPager.setAdapter(pagerAdapter);
        holder.userNameTV.setText(mUserName.get(position));
        holder.dateTV.setText(mDate.get(position));
        holder.userNameTV.setText(mUserName.get(position));
        holder.commentTV.setText(mComment.get(position));
        holder.likeCountTV.setText("+" + mLikeCount.get(position));
        holder.contentTV.setText(mContent.get(position));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mImageSrcs.size();
    }
}
