package com.biswanath.promhighschoolhs.ClassTwelve;




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


public class ClassTwelveFragment extends Fragment {
    private Window _window;
    private RecyclerView category_item_recycler_view;
    private List<ClassTwelvePojo> categories_pojos;
    private TextView name_tv,roll_tv,section_tv,whichClass;
    private CircleImageView imageView;

    private DatabaseReference mdatabse;
    private String mvname="XX";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_twelve, container, false);

        mdatabse=FirebaseDatabase.getInstance().getReference();

        _window=getActivity().getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //this code allays mobile screen light on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on


        category_item_recycler_view = view.findViewById(R.id.category_item_recycler_view);
        whichClass = view.findViewById(R.id.whichClass);
        imageView = view.findViewById(R.id.studentImage);
        name_tv = view.findViewById(R.id.name);
        roll_tv = view.findViewById(R.id.roll);
        section_tv = view.findViewById(R.id.section);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        ImageSlider mainslider = (ImageSlider) view.findViewById(R.id.image_slider);

        final List<SlideModel> remoteimages = new ArrayList<>();


        FirebaseDatabase.getInstance().getReference().child("ClassTwelveSlider")
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


        categories_pojos = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        category_item_recycler_view.setLayoutManager(gridLayoutManager);

        final ClassTwelveAdapter categories_adapter = new ClassTwelveAdapter(getContext(), categories_pojos);
        category_item_recycler_view.setAdapter(categories_adapter);

        myRef.child("ClassTwelveCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Snapshot : snapshot.getChildren()){
                    categories_pojos.add(Snapshot.getValue(ClassTwelvePojo.class));
                    //_loadingDialog.dismiss();
                }
                categories_adapter.notifyDataSetChanged();
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