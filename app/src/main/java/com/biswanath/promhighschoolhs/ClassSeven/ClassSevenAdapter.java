package com.biswanath.promhighschoolhs.ClassSeven;

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


public class ClassSevenAdapter extends RecyclerView.Adapter<ClassSevenAdapter.ClassSeven_viewHolder>{
    private Context context;
    private List<ClassSevenPojo> classSevenPojoList;

    public ClassSevenAdapter(Context context, List<ClassSevenPojo> classSevenPojoList) {
        this.context = context;
        this.classSevenPojoList = classSevenPojoList;
    }

    @NonNull
    @Override
    public ClassSeven_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_item_view,parent,false);
        return new ClassSeven_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassSeven_viewHolder holder, int position) {
        holder.setCategory(classSevenPojoList.get(position).getUrl(),classSevenPojoList.get(position).getName(),classSevenPojoList.get(position).getSet());
        String _names = classSevenPojoList.get(position).getName();
        int  _set  = classSevenPojoList.get(position).getSet();
        String _url = classSevenPojoList.get(position).getUrl();
        holder.setCategory(_names,_url,_set);
    }

    @Override
    public int getItemCount() {
        return classSevenPojoList.size();
    }

    public class ClassSeven_viewHolder extends RecyclerView.ViewHolder {
        CircleImageView _url;
        TextView _title;
        public ClassSeven_viewHolder(@NonNull View itemView) {
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
                    setintent.putExtra("chapterName","ClassSevenChapter");
                    setintent.putExtra("title",title);
                    setintent.putExtra("sets",set);
                    itemView.getContext().startActivity(setintent);
                }
            });

        }
    }
}
