package com.biswanath.promhighschoolhs.ClassFive;




import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.loadRewardedAd;
import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.mRewardedAd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.biswanath.promhighschoolhs.AutoStartVideoActivity;
import com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys;
import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.YoutubeVideoActivity;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ClassFiveConceptAdapter extends RecyclerView.Adapter<ClassFiveConceptAdapter.ClassFiveConceptViewHolder>{
    private Context context;
    private List<ClassFiveConceptPojo> list;
    private String category;

    public ClassFiveConceptAdapter(Context context, List<ClassFiveConceptPojo> list, String category) {
        this.context = context;
        this.list = list;
        this.category = category;
    }

    @NonNull
    @Override
    public ClassFiveConceptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_concept,parent,false);
        return new ClassFiveConceptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassFiveConceptViewHolder holder, int position) {

        String title = list.get(position).getConcept();
        String url = list.get(position).getUrl();
        holder.setData(title,url,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ClassFiveConceptViewHolder extends RecyclerView.ViewHolder {

        private TextView ConceptTitle;
        public ClassFiveConceptViewHolder(@NonNull View itemView) {
            super(itemView);
            ConceptTitle = itemView.findViewById(R.id.conceptTitle);
        }

        private void setData(String title,String url,int position) {

            this.ConceptTitle.setText(position+1+". "+title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent setintent = new Intent(itemView.getContext(), AutoStartVideoActivity.class);
                    Intent setintent = new Intent(itemView.getContext(), YoutubeVideoActivity.class);
                    setintent.putExtra("title",title);
                    setintent.putExtra("videoUrl",url);
                    itemView.getContext().startActivity(setintent);
                    loadRewardedAd(itemView.getContext());

                }
            });

        }
    }


}
