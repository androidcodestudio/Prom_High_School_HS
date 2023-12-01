package com.biswanath.promhighschoolhs;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.biswanath.promhighschoolhs.adapters.ChatPageAdapter;
import com.biswanath.promhighschoolhs.register.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SmsFragment extends Fragment {

    FirebaseAuth myAuth;

    RecyclerView _recyclerView;

    FirebaseDatabase firebaseDatabase;
    final ExecutorService executorService = Executors.newSingleThreadExecutor();
    ChatPageAdapter chatPageAdapter;
    ArrayList<UserModel> userData = new ArrayList<>();
    Toolbar myToolbar;
    String userId;

    TextView _tutorial;
    FloatingActionButton _floatingActionButton,_move_to_myAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sms, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myAuth = FirebaseAuth.getInstance();

         userId = myAuth.getCurrentUser().getUid();
        _tutorial = view.findViewById(R.id.tutorial);
        _floatingActionButton = view.findViewById(R.id.move_to_contactlist_fab);
        _move_to_myAccount = view.findViewById(R.id.move_to_myAccount);
        _recyclerView = view.findViewById(R.id.chats_recyclerview);
        _tutorial.setVisibility(View.GONE);

        if(!isOnline()){
            Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }

        _floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContactListsActivity.class);
                startActivity(intent);
            }
        });

        _move_to_myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        chatPageAdapter = new ChatPageAdapter(userData, getContext());
        executorService.execute(new Runnable() {
            @Override
            public void run() {



                firebaseDatabase.getReference("Users").addValueEventListener(new ValueEventListener() {



                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {



                        userData.clear();
                        ArrayList<String> contactIds = new ArrayList<>();
                        ArrayList<Long> recentMsgTimes = new ArrayList<>();
                        ArrayList<String> recentMsg = new ArrayList<>();


                        if(snapshot.child(userId).hasChild("Contacts"))
                            for (DataSnapshot e : snapshot.child(myAuth.getUid()).child("Contacts").getChildren()){
                                contactIds.add(e.getKey());


                                if(e.hasChild("interactionTime")) {
                                    recentMsgTimes.add((long)e.child("interactionTime").getValue());
                                }

                                if(e.hasChild("recentMessage")){
                                    recentMsg.add(e.child("recentMessage").getValue().toString());
                                }

                            }

                        if(contactIds.isEmpty()){
                            _tutorial.setVisibility(View.VISIBLE);
                        }else{
                            _tutorial.setVisibility(View.GONE);

                        }


                        for(int i=0;i<contactIds.size();i++) {

                            String e = contactIds.get(i);
                            long time = 0;
                            String recentmsg = "";

                            try{
                                if(!recentMsgTimes.isEmpty()){time = recentMsgTimes.get(i);}
                                if(!recentMsg.isEmpty()){recentmsg = recentMsg.get(i);}
                            }catch (IndexOutOfBoundsException err){

                            }




                            String uName = snapshot.child(e).child("userName").getValue().toString();
                            String uMail = snapshot.child(e).child("userMail").getValue().toString();
                            String uPic = snapshot.child(e).child("profilePic").getValue().toString();
                            //todo nothing to null exception
                            String token = snapshot.child(e).child("token").getValue().toString();

                            UserModel model = new UserModel(uName, uMail, uPic);
                            model.setUserId(e);
                            model.setRecentMsgTime(time);
                            model.setToken(token);
                            model.setRecentMessage(recentmsg);
                            userData.add(model);
                            chatPageAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        DividerItemDecoration decoration = new DividerItemDecoration(_recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        _recyclerView.addItemDecoration(decoration);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        _recyclerView.setAdapter(chatPageAdapter);

        chatPageAdapter.setOnItemClickListener(new ChatPageAdapter.OnClickListener() {
            @Override
            public void onItemClick(UserModel userdata) {


                Intent intent = new Intent(getContext(), MessagingActivity.class);
                intent.putExtra("USERNAME", userdata.getUserName());
                intent.putExtra("PROFILEIMAGE", userdata.getProfilePic());
                intent.putExtra("USERID", userdata.getUserId());
                intent.putExtra("TOKEN", userdata.getToken());
                startActivity(intent);


            }
        });


        return view;
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}