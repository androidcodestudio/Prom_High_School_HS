package com.biswanath.promhighschoolhs.StudentsTimeLine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.UserNotice.NoticeAdapter;
import com.biswanath.promhighschoolhs.UserNotice.NoticePojo;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentTimeLineFragment extends Fragment {
    FirebaseAuth myAuth;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference reference;

    String userId;
    RecyclerView _studentTimeLineList;
    ArrayList<StudentTimeLinePojo> list;
    StudentTimeLineAdapter adapter;
    CircleImageView _userIcon;

    EditText _post_title_edt;

    ImageView _timeline_image;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_time_line, container, false);

        //initialization
        _studentTimeLineList = view.findViewById(R.id.student_timeline);
        _userIcon = view.findViewById(R.id.user_icon);
        _post_title_edt = view.findViewById(R.id.post_title_edt);
        _timeline_image = view.findViewById(R.id.timeline_image);
        //initialization

        //firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        myAuth = FirebaseAuth.getInstance();
        userId = myAuth.getCurrentUser().getUid();
        //firebase database

        firebaseDatabase.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uPic = snapshot.child(userId).child("profilePic").getValue().toString();
                Glide.with(getContext()).load(uPic).into(_userIcon);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("PromStudentTimeLine");
        _studentTimeLineList.setLayoutManager(new LinearLayoutManager(getContext()));
        _studentTimeLineList.setHasFixedSize(true);

        getNotice();

        return view;
    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    StudentTimeLinePojo data = dataSnapshot.getValue(StudentTimeLinePojo.class);
                    list.add(0,data);

                }
                adapter = new StudentTimeLineAdapter(list, getContext());
                adapter.notifyDataSetChanged();

                _studentTimeLineList.setAdapter(adapter);


            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}