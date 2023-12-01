package com.biswanath.promhighschoolhs.ClassFive;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.airbnb.lottie.LottieAnimationView;
import com.biswanath.promhighschoolhs.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ClassFiveClassesFragment extends Fragment {
    private RecyclerView ChapterRecyclerView;
    private DatabaseReference reference;
    private List<ClassFiveClassesPojo> list;
    private ClassFiveClassesAdapter adapter;
    //private AdView mAdView;
    private ShimmerFrameLayout container;
    private LinearLayout shimmer_layout;

    private LottieAnimationView _empty_chapter;
    private TextView no_chapter;
    String _subjectName;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_five_classes, container, false);
        //this code allays mobile screen light on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        _subjectName = requireActivity().getIntent().getStringExtra("title");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setMessage("Loading Chapter....");

        _empty_chapter = (com.airbnb.lottie.LottieAnimationView) view.findViewById(R.id.animation);
        no_chapter = view.findViewById(R.id.no_order_text);


        ChapterRecyclerView = view.findViewById(R.id.chapter_item_recycler_view);


        reference = FirebaseDatabase.getInstance().getReference()
                .child(getActivity().getIntent().getStringExtra("chapterName"))
                .child(getActivity().getIntent().getStringExtra("title"));
        getData();
        return view;
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    ClassFiveClassesPojo pojo = snapshot1.getValue(ClassFiveClassesPojo.class);
                    list.add(pojo);
                }
                if (list.size() == 0){
                    progressDialog.dismiss();
                    _empty_chapter.setVisibility(View.VISIBLE);
                    ChapterRecyclerView.setVisibility(View.GONE);
                    no_chapter.setVisibility(View.VISIBLE);
                    //location_dialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    _empty_chapter.setVisibility(View.GONE);
                    no_chapter.setVisibility(View.GONE);
                    ChapterRecyclerView.setVisibility(View.VISIBLE);

                }
                progressDialog.dismiss();
                adapter = new ClassFiveClassesAdapter(getContext(),list);
                ChapterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                ChapterRecyclerView.setAdapter(adapter);
//                container.stopShimmer();
//                shimmer_layout.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}