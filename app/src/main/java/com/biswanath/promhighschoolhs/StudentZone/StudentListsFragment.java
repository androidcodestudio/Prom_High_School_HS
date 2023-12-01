package com.biswanath.promhighschoolhs.StudentZone;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.biswanath.promhighschoolhs.R;

import com.biswanath.promhighschoolhs.register.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class StudentListsFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<UserModel> list;
    UserAdapter adapter;
    DatabaseReference reference;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_student_lists, container, false);

        //this code allays mobile screen light on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        recyclerView = view.findViewById(R.id.review);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        getAllStudent();


        //---------------------swipe refresh layout---------------------
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                list.clear();
                getAllStudent();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        //---------------------swipe refresh layout---------------------
        return view;
    }

    private void getAllStudent() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel data = dataSnapshot.getValue(UserModel.class);
                    list.add(0,data);

                }
                adapter = new UserAdapter(getContext(),list);
                adapter.notifyDataSetChanged();

                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}