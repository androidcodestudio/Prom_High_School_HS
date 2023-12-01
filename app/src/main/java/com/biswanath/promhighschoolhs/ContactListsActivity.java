package com.biswanath.promhighschoolhs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biswanath.promhighschoolhs.register.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ContactListsActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    ArrayList<UserModel> searchedUser = new ArrayList<>(1);

    RelativeLayout _new_user_display;

    SearchView _searchView;
    TextView _user_name,_user_mail;

    ImageView _profile_pic_imageview,_add_contact_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_lists);

        _new_user_display = findViewById(R.id.new_user_display);
        _new_user_display.setVisibility(View.GONE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        _searchView = findViewById(R.id.search_view);
        _user_name = findViewById(R.id.user_name);
        _user_mail = findViewById(R.id.usermail);
        _profile_pic_imageview = findViewById(R.id.profile_pic_imageview);
        _add_contact_btn = findViewById(R.id.add_contact_btn);

        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                _searchView.clearFocus();
                searchedUser.clear();

                firebaseDatabase.getReference("Users")
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        boolean flag = false;

                        for(DataSnapshot e : snapshot.getChildren()){

                            flag = e.getKey().equals(firebaseAuth.getCurrentUser().getUid());


                            if(!flag && e.child("name").getValue().equals(query) ) {


                                Log.d("testcontact"," "+query+"  name= "+e.child("userName").getValue().toString());
                                UserModel userModel = new UserModel();
                                userModel.setUserName(e.child("userName").getValue().toString());

                                userModel.setUserId(e.getKey());
                                searchedUser.add(userModel);

                                _user_name.setText(e.child("userName").getValue().toString());
                                _user_mail.setText(e.child("userMail").getValue().toString());
                                String pic = e.child("profilePic").getValue().toString();
                                Picasso.get().load(pic)
                                        .fit()
                                        .centerCrop()
                                        .error(R.drawable.user)
                                        .placeholder(R.drawable.user)
                                        .into(_profile_pic_imageview);

                                _new_user_display.setVisibility(View.VISIBLE);

                                _add_contact_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Toast.makeText(ContactListsActivity.this, "Contact added", Toast.LENGTH_SHORT).show();

                                        String userId = searchedUser.get(0).getUserId();

                                        firebaseDatabase.getReference("Users")
                                                .child(firebaseAuth.getCurrentUser().getUid())
                                                .child("Contacts")
                                                .child(userId)
                                                .setValue("Chats");

                                        searchedUser.clear();

                                        firebaseDatabase.getReference("Users")
                                                .child(firebaseAuth.getUid())
                                                .child("Contacts")
                                                .child(userId)
                                                .child("interactionTime")
                                                .setValue(new Date().getTime());

                                        firebaseDatabase.getReference("Users")
                                                .child(firebaseAuth.getUid())
                                                .child("Contacts")
                                                .child(userId)
                                                .child("recentMessage")
                                                .setValue("");

                                    }
                                });

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        searchedUser.clear();
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}