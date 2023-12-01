package com.biswanath.promhighschoolhs.ClassNine;

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


public class ClassNineAdapter extends RecyclerView.Adapter<ClassNineAdapter.ClassNine_viewHolder>{
    private Context context;
    private List<ClassNinePojo> classNinePojoList;

    public ClassNineAdapter(Context context, List<ClassNinePojo> classNinePojoList) {
        this.context = context;
        this.classNinePojoList = classNinePojoList;
    }

    @NonNull
    @Override
    public ClassNine_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_item_view,parent,false);
        return new ClassNine_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassNine_viewHolder holder, int position) {
        holder.setCategory(classNinePojoList.get(position).getUrl(),classNinePojoList.get(position).getName(),classNinePojoList.get(position).getSet());
        String _names = classNinePojoList.get(position).getName();
        int  _set  = classNinePojoList.get(position).getSet();
        String _url = classNinePojoList.get(position).getUrl();
        holder.setCategory(_names,_url,_set);
    }

    @Override
    public int getItemCount() {
        return classNinePojoList.size();
    }

    public class ClassNine_viewHolder extends RecyclerView.ViewHolder {
        CircleImageView _url;
        TextView _title;
        public ClassNine_viewHolder(@NonNull View itemView) {
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
                    setintent.putExtra("chapterName","ClassNineChapter");
                    setintent.putExtra("title",title);
                    setintent.putExtra("sets",set);
                    itemView.getContext().startActivity(setintent);
                }
            });

        }
    }
}
