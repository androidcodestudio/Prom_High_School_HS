package com.biswanath.promhighschoolhs.ClassTen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.biswanath.promhighschoolhs.ClassFive.TabActivity;
import com.biswanath.promhighschoolhs.R;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassTenAdapter extends RecyclerView.Adapter<ClassTenAdapter.ClassTen_viewHolder>{
    private Context context;
    private List<ClassTenPojo> classTenPojoList;

    public ClassTenAdapter(Context context, List<ClassTenPojo> classTenPojoList) {
        this.context = context;
        this.classTenPojoList = classTenPojoList;
    }

    @NonNull
    @Override
    public ClassTen_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_item_view,parent,false);
        return new ClassTen_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassTen_viewHolder holder, int position) {
        holder.setCategory(classTenPojoList.get(position).getUrl(),classTenPojoList.get(position).getName(),classTenPojoList.get(position).getSet());
        String _names = classTenPojoList.get(position).getName();
        int  _set  = classTenPojoList.get(position).getSet();
        String _url = classTenPojoList.get(position).getUrl();
        holder.setCategory(_names,_url,_set);
    }

    @Override
    public int getItemCount() {
        return classTenPojoList.size();
    }

    public class ClassTen_viewHolder extends RecyclerView.ViewHolder {
        CircleImageView _url;
        TextView _title;
        public ClassTen_viewHolder(@NonNull View itemView) {
            super(itemView);
            _url = itemView.findViewById(R.id.category_icon);
            _title = itemView.findViewById(R.id.category_title);
        }
        private void setCategory(final String title,final String url,final int set){
            Glide.with(context).load(url).into(_url);
            _title.setText(title);
            _title.setSelected(true);

//            final Dialog loadingDialog = new Dialog(context);
//            loadingDialog.setContentView(R.layout.loading);
//            loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
//            loadingDialog.setCancelable(false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    loadingDialog.show();
                    Intent setintent = new Intent(itemView.getContext(), TabActivity.class);
                    setintent.putExtra("chapterName","ClassTenChapter");
                    setintent.putExtra("title",title);
                    setintent.putExtra("sets",set);
                    itemView.getContext().startActivity(setintent);
                }
            });

        }
    }
}
