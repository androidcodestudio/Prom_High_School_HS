package com.biswanath.promhighschoolhs.StudentsTimeLine;

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

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentTimeLineAdapter extends RecyclerView.Adapter<StudentTimeLineAdapter.StudentTimeLineViewholder>{
    private ArrayList<StudentTimeLinePojo> list;
    private Context context;

    public StudentTimeLineAdapter(ArrayList<StudentTimeLinePojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentTimeLineAdapter.StudentTimeLineViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_news_feed_item, parent, false);
        return new StudentTimeLineAdapter.StudentTimeLineViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentTimeLineAdapter.StudentTimeLineViewholder holder, int position) {
        StudentTimeLinePojo currentItem = list.get(position);
        Glide.with(context).load(currentItem.getUserIcon()).into(holder.userIcon);
        holder.userName.setText(currentItem.getUserName());
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

    public class StudentTimeLineViewholder extends RecyclerView.ViewHolder {

        CircleImageView userIcon;
        TextView userName,deleteNoticeTitle, date, time;
        ImageView deleteNoticeImage;
        public StudentTimeLineViewholder(@NonNull View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.user_icons);
            userName = itemView.findViewById(R.id.user_name);

            deleteNoticeTitle = itemView.findViewById(R.id.deleteNoticeTitle);
            deleteNoticeImage = itemView.findViewById(R.id.deleteNoticeImage);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }
    }
}
