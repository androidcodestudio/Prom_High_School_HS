package com.biswanath.promhighschoolhs.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.YoutubeVideoActivity;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    ImageView map;

    ImageSlider slider;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_home, container, false);

        map = view.findViewById(R.id.map);
        slider = (ImageSlider) view.findViewById(R.id.slider);
        final List<SlideModel> images = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("PROME_HIGH_SCHOOL_SLIDER_IMAGE")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            images.add(0,new SlideModel(
                                    data.child("url").getValue().toString()
                                    ,data.child("title").getValue().toString()

                                    , ScaleTypes.FIT));

                            slider.setImageList(images, ScaleTypes.FIT);

                            slider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void doubleClick(int i) {


                                }

                                @Override
                                public void onItemSelected(int i) {
                                    Intent intent = new Intent(getContext(), YoutubeVideoActivity.class);
                                    intent.putExtra("videoUrl",images.get(i).getTitle().toString());
                                    startActivity(intent);


                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.app.goo.gl/6wjVfLdeu3GkpUbN9")));
            }
        });

       return view;
    }

}