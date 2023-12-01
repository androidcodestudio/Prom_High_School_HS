package com.biswanath.promhighschoolhs.LiveClass;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LiveClassFragment extends Fragment {
    private RecyclerView ChapterRecyclerView;
    private DatabaseReference reference;
    private List<LiveClassPojo> list;
    private LiveClassAdapter adapter;
    private ShimmerFrameLayout container;
    private LinearLayout shimmer_layout;

    private LottieAnimationView _empty_chapter;
    private TextView no_chapter;
    String _subjectName;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_live_class, container, false);
        _subjectName = requireActivity().getIntent().getStringExtra("title");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setMessage("Loading Upcoming Live Class....");

        //this code allays mobile screen light on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        ChapterRecyclerView = view.findViewById(R.id.chapter_item_recycler_view);
        //swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        _empty_chapter = (LottieAnimationView) view.findViewById(R.id.animation);
        no_chapter = view.findViewById(R.id.no_order_text);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        list = new ArrayList<>();
        ChapterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LiveClassAdapter(getContext(),list);
        ChapterRecyclerView.setAdapter(adapter);

        myRef.child("UpcomingNote")
                .child(_subjectName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot Snapshot : snapshot.getChildren()){
                            list.add(new LiveClassPojo(
                                    Snapshot.child("UpcomingClassName").getValue().toString()
                                    ,Snapshot.child("Date").getValue().toString()
                                    ,Snapshot.child("Time").getValue().toString()));
                        }
                        //adapter.notifyDataSetChanged();
//                        if (adapter != null){
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
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }
}