package com.biswanath.promhighschoolhs.ClassFive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.biswanath.promhighschoolhs.R;

import java.util.List;


public class ClassFiveClassesAdapter extends RecyclerView.Adapter<ClassFiveClassesAdapter.ClassFiveViewHolder>{
    private Context context;
    private List<ClassFiveClassesPojo> list;

    public ClassFiveClassesAdapter(Context context, List<ClassFiveClassesPojo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ClassFiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter,parent,false);
        return new ClassFiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassFiveViewHolder holder, int position) {
        String title = list.get(position).getName();
        int set = list.get(position).getSet();
        holder.setData(title,set,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ClassFiveViewHolder extends RecyclerView.ViewHolder {
        private TextView ChapterTitle;
        public ClassFiveViewHolder(@NonNull View itemView) {
            super(itemView);

            ChapterTitle = itemView.findViewById(R.id.chapterTitle);

        }

        public void setData(String title, int set, int position) {

            this.ChapterTitle.setText(position+1+". "+title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setintent = new Intent(itemView.getContext(), ClassFiveConceptActivity.class);
                    setintent.putExtra("category",title);
                    setintent.putExtra("setNo",set);
                    itemView.getContext().startActivity(setintent);
                }
            });
        }
    }
}
