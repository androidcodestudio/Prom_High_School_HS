package com.biswanath.promhighschoolhs.ui.notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.UserNotice.NoticeAdapter;
import com.biswanath.promhighschoolhs.UserNotice.NoticePojo;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NoticeFragment extends Fragment {

    RecyclerView NoticeRecycler;
    ArrayList<NoticePojo> list;
    NoticeAdapter adapter;
    FloatingActionButton fab;
    DatabaseReference reference;

    private ShimmerFrameLayout shimmer_container;
    private LinearLayout shimmer_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        shimmer_container =  view.findViewById(R.id.shimmer_view_container);
        shimmer_layout = view.findViewById(R.id.shimmer_layout);
        shimmer_container.startShimmer(); // If auto-start is set to false

        NoticeRecycler = view.findViewById(R.id.deleteNoticeRecycler);


        reference = FirebaseDatabase.getInstance().getReference().child("PromNotice");

        NoticeRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        NoticeRecycler.setHasFixedSize(true);

        getNotice();
        return view;
    }
    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NoticePojo data = dataSnapshot.getValue(NoticePojo.class);
                    list.add(0,data);

                }
                adapter = new NoticeAdapter(list, getContext());
                adapter.notifyDataSetChanged();

                NoticeRecycler.setAdapter(adapter);
                shimmer_container.stopShimmer();
                shimmer_layout.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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