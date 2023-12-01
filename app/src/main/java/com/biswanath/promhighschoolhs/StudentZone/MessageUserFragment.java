package com.biswanath.promhighschoolhs.StudentZone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biswanath.promhighschoolhs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class MessageUserFragment extends Fragment {

    RecyclerView _chatList;
    ArrayList<String> list;
    UserAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_zone, container, false);

        _chatList = view.findViewById(R.id.chat_recyclerView);
        getData();
        return view;
    }

    private void getData(){
        ArrayList<String> oldlist  = new ArrayList<String>();
        ArrayList<String> newlist  = new ArrayList<String>();

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            String currentId  = task.getResult().getString("phone");

                            FirebaseDatabase.getInstance().getReference("Chats")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                if (dataSnapshot.getKey().contains(currentId)){
                                                    oldlist.add(dataSnapshot.getKey().replace(currentId,""));
                                                    newlist.add(dataSnapshot.getKey());
                                                }
//                                                adapter = new UserAdapter(oldlist, newlist);
//                                                adapter.notifyDataSetChanged();
//                                                NoticeRecycler.setAdapter(adapter);

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getContext(), "something went worng", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}