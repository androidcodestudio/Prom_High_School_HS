package com.biswanath.promhighschoolhs.ClassFive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.airbnb.lottie.LottieAnimationView;
import com.biswanath.promhighschoolhs.ClassFiveSet.ClassFiveSetGridAdapter;
import com.biswanath.promhighschoolhs.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.database.DatabaseReference;

public class ClassFiveMockTestFragment extends Fragment {
    private GridView set_item_grid_view;
    private DatabaseReference mdatabse;
    private String mvname="XX";

    private LottieAnimationView _empty_chapter;
    private TextView no_chapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_five_mock_test, container, false);
        //mdatabse= FirebaseDatabase.getInstance().getReference();

        //this code allays mobile screen light on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        set_item_grid_view = view.findViewById(R.id.set_item_grid_view);
        _empty_chapter = (com.airbnb.lottie.LottieAnimationView) view.findViewById(R.id.animation);
        no_chapter = view.findViewById(R.id.no_order_text);

        if (getActivity().getIntent().getIntExtra("sets",0) == 0){
            _empty_chapter.setVisibility(View.VISIBLE);
            set_item_grid_view.setVisibility(View.GONE);
            no_chapter.setVisibility(View.VISIBLE);
        }else{
            _empty_chapter.setVisibility(View.GONE);
            no_chapter.setVisibility(View.GONE);
            set_item_grid_view.setVisibility(View.VISIBLE);
        }

        ClassFiveSetGridAdapter gridAdapter = new ClassFiveSetGridAdapter(getActivity().getIntent().getIntExtra("sets",0), getActivity().getIntent().getStringExtra("title"));
        set_item_grid_view.setAdapter(gridAdapter);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

//        mdatabse.child("Demo").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//
//                    mvname=dataSnapshot.child("uid").getValue().toString();
//
//                    if (mvname.equals("kLK9gbSyaodhdPA5F1ofNPOA6LH3")){
//
//                        FirebaseFirestore.getInstance().collection("Demo").document(mvname)
//                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            //DataBaseQuerys.phone = task.getResult().getString("phone");
//                                            DataBaseQuerys.Image = task.getResult().getString("Image");
//                                            DataBaseQuerys.Name = task.getResult().getString("Name");
//                                            DataBaseQuerys.roll = task.getResult().getString("Roll");
//                                            DataBaseQuerys.section = task.getResult().getString("Section");
//
//                                            Glide.with(getContext()).load(DataBaseQuerys.Image).into(imageView);
//                                            name_tv.setText("Name: "+String.format(DataBaseQuerys.Name));
//                                            roll_tv.setText("Roll: "+String.format(DataBaseQuerys.roll));
//                                            section_tv.setText("Section: "+String.format(DataBaseQuerys.section));
//
//                                        } else {
//                                            String error = task.getException().getMessage();
//                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }else {
//
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            //DataBaseQuerys.phone = task.getResult().getString("phone");
//                            DataBaseQuerys.Image = task.getResult().getString("Image");
//                            DataBaseQuerys.Name = task.getResult().getString("Name");
//                            DataBaseQuerys.roll = task.getResult().getString("Roll");
//                            DataBaseQuerys.section = task.getResult().getString("Section");
//
//                            Glide.with(getContext()).load(DataBaseQuerys.Image).into(imageView);
//                            name_tv.setText("Name: "+String.format(DataBaseQuerys.Name));
//                            roll_tv.setText("Roll: "+String.format(DataBaseQuerys.roll));
//                            section_tv.setText("Section: "+String.format(DataBaseQuerys.section));
//
//                        } else {
//                            String error = task.getException().getMessage();
//                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

//        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            //DataBaseQuerys.phone = task.getResult().getString("phone");
//                            DataBaseQuerys.Image = task.getResult().getString("Image");
//                            DataBaseQuerys.Name = task.getResult().getString("Name");
//                            DataBaseQuerys.roll = task.getResult().getString("Roll");
//                            DataBaseQuerys.section = task.getResult().getString("Section");
//
//                            Glide.with(getContext()).load(DataBaseQuerys.Image).into(imageView);
//                            name_tv.setText("Name: "+String.format(DataBaseQuerys.Name));
//                            roll_tv.setText("Roll: "+String.format(DataBaseQuerys.roll));
//                            section_tv.setText("Section: "+String.format(DataBaseQuerys.section));
//
//                        } else {
//                            String error = task.getException().getMessage();
//                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }
}