package com.biswanath.promhighschoolhs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biswanath.promhighschoolhs.register.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    FirebaseStorage firebaseStorage;
    TextView _uid,_username,_text,_about,_newPic,_usermail,_logout;
    LinearLayout _edit_frag_container;
    EditText _edittext;
    Button _save_edit_btn;
    ImageView _profile_pic_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        String uid = firebaseAuth.getUid();
        _uid = findViewById(R.id.uid);
        _username = findViewById(R.id.username);
        _edit_frag_container = findViewById(R.id.edit_frag_container);
        _edittext = findViewById(R.id.edittext);
        _text = findViewById(R.id.text);
        _about = findViewById(R.id.about);
        _save_edit_btn = findViewById(R.id.save_edit_btn);
        _newPic = findViewById(R.id.newPic);
        _profile_pic_imageview = findViewById(R.id.profile_pic_imageview);
        _usermail = findViewById(R.id.usermail);
        _logout = findViewById(R.id.logout);


       _uid.setText(uid);

        _username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _edit_frag_container.setVisibility(View.VISIBLE);
                _edit_frag_container.bringToFront();
                _uid.setVisibility(View.GONE);

                _text.setText("Enter new name");

                _edittext.requestFocus();
                _save_edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ss = _edittext.getText().toString().trim();




                        firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("userName").setValue(ss).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(ProfileActivity.this, "Username editted ", Toast.LENGTH_SHORT).show();
                                _edittext.setText("");
                                _edit_frag_container.setVisibility(View.GONE);
                            }
                        });



                    }
                });

            }
        });

        _about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _edit_frag_container.setVisibility(View.VISIBLE);
                _edit_frag_container.bringToFront();
                _uid.setVisibility(View.GONE);


                _text.setText("Enter about");
                _edittext.setHint("about");

                _edittext.requestFocus();
                _save_edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ss = _edittext.getText().toString().trim();

                        firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("about").setValue(ss);
                        _edittext.setText("");
                        _edit_frag_container.setVisibility(View.GONE);

                    }
                });

            }
        });




        _newPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setAspectRatio(3,3)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true).setOutputCompressQuality(60)
                        .start(ProfileActivity.this);


            }
        });

        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.getReference("Users").child(firebaseAuth.getUid())
                        .child("token").setValue("");
                firebaseAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });




        firebaseDatabase.getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String uName = snapshot.child("userName").getValue().toString();
                String uMail = snapshot.child("userMail").getValue().toString();
                String uPic = snapshot.child("profilePic").getValue().toString();
                String uAbout = snapshot.child("about").getValue().toString();

                Picasso.get().load(uPic).error(R.drawable.user)
                        .placeholder(R.drawable.user).centerCrop().fit()
                        .into(_profile_pic_imageview);

                _username.setText(uName);
                _usermail.setText(uMail);
                _about.setText(uAbout);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();


                Picasso.get().load(resultUri).fit().centerCrop().into(_profile_pic_imageview);

                final StorageReference storageRef = firebaseStorage.getReference().child("Profile pictures").child(firebaseAuth.getUid());
                storageRef.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("profilePic").setValue(uri.toString());

                            }
                        });

                    }
                });


            }
        }

    }
}