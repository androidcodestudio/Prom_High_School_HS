package com.biswanath.promhighschoolhs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import android.content.res.Configuration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biswanath.promhighschoolhs.adapters.MessageAdapter;
import com.biswanath.promhighschoolhs.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class MessagingActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    public String receiverId;
    String receiverToken, senderName;
    String senderId;

    RelativeLayout _parentViewgroup;
    TextView _receiver_name;
    ImageView _profilePicImageview,_back_btn,_send_msg_btn;

    RecyclerView _msg_recyclerview;
    EditText _typing_space;

     String decrypted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        _parentViewgroup = findViewById(R.id.parent_viewgroup);
        _receiver_name = findViewById(R.id.receiver_name);
        _profilePicImageview = findViewById(R.id.profile_pic_imageview);
        _back_btn = findViewById(R.id.back_btn);
        _msg_recyclerview = findViewById(R.id.msg_recyclerview);
        _typing_space = findViewById(R.id.post_title_edt);
        _send_msg_btn = findViewById(R.id.send_msg_btn);


        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                _parentViewgroup.setBackground(AppCompatResources.getDrawable(MessagingActivity.this,R.drawable.wpdark));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                _parentViewgroup.setBackground(AppCompatResources.getDrawable(MessagingActivity.this,R.drawable.wplight));
                break;
        }

        senderId = firebaseAuth.getUid();

        Intent intent = getIntent();
        String uname = intent.getStringExtra("USERNAME");
        String profileImg = intent.getStringExtra("PROFILEIMAGE");
        receiverId = intent.getStringExtra("USERID");
        receiverToken = intent.getStringExtra("TOKEN");


        _receiver_name.setText(uname);
        Picasso.get().load(profileImg).fit().centerCrop()
                .error(R.drawable.user)
                .placeholder(R.drawable.user)
                .into(_profilePicImageview);


        _back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MessagingActivity.this,MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });


        final ArrayList<MessageModel> msgData = new ArrayList<>();
        final MessageAdapter msgAdapter = new MessageAdapter(msgData,MessagingActivity.this);
        _msg_recyclerview.setAdapter(msgAdapter);
        _msg_recyclerview.setLayoutManager(new LinearLayoutManager(this));


        firebaseDatabase.getReference("Users")
                .child(senderId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){

                        senderName = dataSnapshot.child("userName").getValue().toString();
                        msgData.clear();

                        for (DataSnapshot e : dataSnapshot.child("Contacts").child(receiverId).child("Chats").getChildren()){


                            String msg = e.child("msgText").getValue().toString();

                            try {
                               decrypted = AESUtils.decrypt(msg);
                            } catch (Exception er) {
                                er.printStackTrace();
                            }

                            msgData.add(new MessageModel(e.child("uId").getValue().toString()
                                    ,decrypted
                                    ,(Long) Long.valueOf(e.child("msgTime").getValue().toString())));

                        }

                        msgAdapter.notifyDataSetChanged();
                        _msg_recyclerview.scrollToPosition(msgAdapter.getItemCount()-1);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });



//        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //Messaging Mechanism
        _send_msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = _typing_space.getText().toString().trim();

                String encryptedMsg = msg;
                try {
                    encryptedMsg = AESUtils.encrypt(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                long date = new Date().getTime();

                _typing_space.setText("");
                final MessageModel messageModel = new MessageModel(senderId, encryptedMsg, date);

                if(!msg.isEmpty()) {
                    firebaseDatabase.getReference("Users").child(senderId).child("Contacts")
                            .child(receiverId).child("Chats").push()
                            .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    _msg_recyclerview.scrollToPosition(msgAdapter.getItemCount()-1);

                                    FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(receiverToken,senderName,msg,getApplicationContext(),MessagingActivity.this);
                                    fcmNotificationsSender.SendNotifications();

                                    firebaseDatabase.getReference("Users").child(receiverId).child("Contacts").child(senderId)
                                            .child("interactionTime").setValue(date);

                                    firebaseDatabase.getReference("Users").child(senderId).child("Contacts").child(receiverId)
                                            .child("interactionTime").setValue(date);


                                    firebaseDatabase.getReference("Users").child(receiverId).child("Contacts")
                                            .child(senderId).child("Chats").push()
                                            .setValue(messageModel);

                                    firebaseDatabase.getReference("Users").child(senderId).child("Contacts").child(receiverId)
                                            .child("recentMessage").setValue(msg);

                                    firebaseDatabase.getReference("Users").child(receiverId).child("Contacts").child(senderId)
                                            .child("recentMessage").setValue(msg);


                                }
                            });
                }


            }
        });



        _msg_recyclerview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override

            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom)
            {

                if ( bottom < oldBottom) {
                    _msg_recyclerview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if((msgAdapter.getItemCount()-1)>1)
                                _msg_recyclerview.smoothScrollToPosition(msgAdapter.getItemCount()-1);
                        }
                    }, 10);
                }

            }
        });
    }
}