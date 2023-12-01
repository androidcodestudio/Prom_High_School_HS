package com.biswanath.promhighschoolhs.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.biswanath.promhighschoolhs.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    RecyclerView vocational_list;
    GalleryAdapter adapter;
    DatabaseReference reference;
    private ShimmerFrameLayout shimmer_container;
    private LinearLayout shimmer_layout,main_con;
//    private TextView vocational_title,civil_title,mobile_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_gallery, container, false);

        shimmer_container =  view.findViewById(R.id.shimmer_view_container);
        shimmer_layout = view.findViewById(R.id.shimmer_layout);
        shimmer_container.startShimmer(); // If auto-start is set to false

        main_con = view.findViewById(R.id.main_con);
        vocational_list = view.findViewById(R.id.vocational_department_list);

        reference = FirebaseDatabase.getInstance().getReference().child("GALLERY");
        getVocationalListImage();
        return view;
    }



    private void getVocationalListImage() {
        reference.child("Prom High School Gallery").addValueEventListener(new ValueEventListener() {
            List<String> imageList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String data = (String) snapshot1.getValue();
                    imageList.add(data);
                }
                adapter = new GalleryAdapter(getContext(),imageList);
                vocational_list.setLayoutManager(new GridLayoutManager(getContext(),3));
                vocational_list.setAdapter(adapter);
                shimmer_container.stopShimmer();
                shimmer_layout.setVisibility(View.GONE);
                main_con.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPause() {
        shimmer_container.stopShimmer();
        super.onPause();
    }

    @Override
    public void onResume() {
        shimmer_container.startShimmer();
        super.onResume();
    }
}