package com.biswanath.promhighschoolhs.LiveClass;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biswanath.promhighschoolhs.R;

import java.util.List;


public class LiveClassAdapter extends RecyclerView.Adapter<LiveClassAdapter.LiveClassViewHolder>{
    private Context context;
    private List<LiveClassPojo> list;

    public LiveClassAdapter(Context context, List<LiveClassPojo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LiveClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.biswanath.promhighschoolhs.R.layout.item_live_class,parent,false);
        return new LiveClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveClassViewHolder holder, int position) {
        String upcomingClassName = list.get(position).getUpcomingClassName();
        String date = list.get(position).getDate();
        String time = list.get(position).getTime();
        holder.setData(upcomingClassName,date,time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LiveClassViewHolder extends RecyclerView.ViewHolder {
        private TextView LiveClassTitle,DateAndTime;
        public LiveClassViewHolder(@NonNull View itemView) {
            super(itemView);
            LiveClassTitle = itemView.findViewById(R.id.LiveClassTitle);
            DateAndTime = itemView.findViewById(R.id.DateAndTime);
        }
        public void setData(String upcomingClassName, String date, String time) {

            if (LiveClassTitle== null){
                LiveClassTitle.setText("class not found");
            }else {
                this.LiveClassTitle.setText(upcomingClassName);
            }

            if (DateAndTime== null){
                DateAndTime.setText("class not found");
            }else {
                this.DateAndTime.setText("Live at "+date+" at "+time);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo if video is live then appear to intent
                    Toast.makeText(context, "Video in not live", Toast.LENGTH_SHORT).show();
//                    Intent setintent = new Intent(itemView.getContext(), ConceptActivity.class);
//                    setintent.putExtra("category",title);
//                    setintent.putExtra("setNo",set);
//                    itemView.getContext().startActivity(setintent);
                }
            });
        }
    }
}
