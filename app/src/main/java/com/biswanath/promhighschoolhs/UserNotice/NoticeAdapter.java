package com.biswanath.promhighschoolhs.UserNotice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biswanath.promhighschoolhs.FullImageViewActivity;
import com.biswanath.promhighschoolhs.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.AdminNoticeViewholder> {
    private ArrayList<NoticePojo> list;
    private Context context;

    public NoticeAdapter(ArrayList<NoticePojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminNoticeViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_news_feed_item, parent, false);
        return new AdminNoticeViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminNoticeViewholder holder, int position) {
        NoticePojo currentItem = list.get(position);
        holder.deleteNoticeTitle.setText(currentItem.getTitle());
        holder.time.setText(currentItem.getTime());
        holder.date.setText(currentItem.getDate());
        Glide.with(context).load(currentItem.getImage()).into(holder.deleteNoticeImage);

        holder.deleteNoticeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullImageViewActivity.class);
                intent.putExtra("image",currentItem.getImage());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdminNoticeViewholder extends RecyclerView.ViewHolder {
        TextView deleteNoticeTitle, date, time;
        ImageView deleteNoticeImage;

        public AdminNoticeViewholder(@NonNull View itemView) {
            super(itemView);
            deleteNoticeTitle = itemView.findViewById(R.id.deleteNoticeTitle);
            deleteNoticeImage = itemView.findViewById(R.id.deleteNoticeImage);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);


        }


    }
}
