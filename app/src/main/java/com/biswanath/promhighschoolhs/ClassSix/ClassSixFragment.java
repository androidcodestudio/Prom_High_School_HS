package com.biswanath.promhighschoolhs.ClassSix;



import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.Image;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys;
import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.YoutubeVideoActivity;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClassSixFragment extends Fragment {
    private RecyclerView category_item_recycler_view;
    private List<ClassSixPojo> classSixPojoList;

    private Window _window;
    private Toolbar toolbar;

    private NestedScrollView main_con;
    //private ConstraintLayout emptyView;


    private TextView name_tv,roll_tv,section_tv;

    private TextView whichClass;
    private CircleImageView imageView;

    private DatabaseReference mdatabse;
    private String mvname="XX";

    //ImageSlider mainslider;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_six, container, false);

        _window=getActivity().getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //this code allays mobile screen light on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        //initialize view
        main_con = view.findViewById(R.id.containor_main_fragment);
        //emptyView = view.findViewById(R.id.no_data);
        category_item_recycler_view = view.findViewById(R.id.category_item_recycler_view);

        ImageSlider mainslider = (ImageSlider) view.findViewById(R.id.image_slider);

        final List<SlideModel> remoteimages = new ArrayList<>();


        FirebaseDatabase.getInstance().getReference().child("ClassSixSlider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            remoteimages.add(0,new SlideModel(
                                    data.child("url").getValue().toString()
                                    ,data.child("title").getValue().toString()

                                    , ScaleTypes.FIT));

                            mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                            mainslider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void doubleClick(int i) {


                                }

                                @Override
                                public void onItemSelected(int i) {
                                    Intent intent = new Intent(getContext(), YoutubeVideoActivity.class);
                                    intent.putExtra("videoUrl",remoteimages.get(i).getTitle().toString());
                                    startActivity(intent);


                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        whichClass = view.findViewById(R.id.whichClass);
        imageView = view.findViewById(R.id.studentImage);

        name_tv = view.findViewById(R.id.name);
        roll_tv = view.findViewById(R.id.roll);
        section_tv = view.findViewById(R.id.section);

        //firebase initialize
        mdatabse=FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        classSixPojoList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        category_item_recycler_view.setLayoutManager(gridLayoutManager);

        final ClassSixAdapter classSixAdapter = new ClassSixAdapter(getContext(), classSixPojoList);
        category_item_recycler_view.setAdapter(classSixAdapter);

        myRef.child("ClassSixCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot Snapshot : snapshot.getChildren()){
                    classSixPojoList.add(Snapshot.getValue(ClassSixPojo.class));
                    //_loadingDialog.dismiss();
                }
                classSixAdapter.notifyDataSetChanged();
//                if(classSixPojoList.size() == 0){
//                    //_loadingDialog.dismiss();
//                    //main_con.setBackgroundColor(Color.parseColor("#e0f2f1"));
//                    category_item_recycler_view.setVisibility(View.GONE);
//                    emptyView.setVisibility(View.VISIBLE);
//                }else{
//                    category_item_recycler_view.setVisibility(View.VISIBLE);
//                    emptyView.setVisibility(View.GONE);
//                    for(DataSnapshot Snapshot : snapshot.getChildren()){
//                        classSixPojoList.add(Snapshot.getValue(ClassSixPojo.class));
//                        //_loadingDialog.dismiss();
//                    }
//                    classSixAdapter.notifyDataSetChanged();
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();


        String form = requireActivity().getIntent().getStringExtra("Test");

        if(form.equals("LoginPurpose")){
            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Image = task.getResult().getString("Image");
                                DataBaseQuerys.whichClass = task.getResult().getString("Class");
                                DataBaseQuerys.Name = task.getResult().getString("Name");
                                DataBaseQuerys.roll = task.getResult().getString("Roll");
                                DataBaseQuerys.section = task.getResult().getString("Section");

                                Glide.with(getContext()).load(Image).into(imageView);
                                whichClass.setText("Class : "+String.format(DataBaseQuerys.whichClass));
                                name_tv.setText("Name : "+String.format(DataBaseQuerys.Name));
                                roll_tv.setText("Roll No : "+String.format(DataBaseQuerys.roll));
                                section_tv.setText("Section : "+String.format(DataBaseQuerys.section));
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        if (form.equals("ForReview")){
            Glide.with(getContext()).load(R.drawable.offical_logo).into(imageView);
            whichClass.setText("Class : xxxx");
            name_tv.setText("Name : xxxx");
            roll_tv.setText("Roll No: xxxx");
            section_tv.setText("Section : xxxx");

        }



    }
}