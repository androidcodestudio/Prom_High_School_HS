package com.biswanath.promhighschoolhs.StudentZone;



import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.UserNotice.NoticeAdapter;
import com.biswanath.promhighschoolhs.UserNotice.NoticePojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private TextInputEditText _messageEdt;
    private ImageView _send;

    RecyclerView recyclerView;
    ArrayList<MessageModel> list;
    MessageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        recyclerView = findViewById(R.id.message_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        recyclerView.setHasFixedSize(true);
        getData(getIntent().getStringExtra("chat_id"));


        _messageEdt = findViewById(R.id.your_message);
        _send = findViewById(R.id.sendMessage);

        _send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_messageEdt.getText().toString().isEmpty()){
                    Toast.makeText(MessageActivity.this, "type a message!", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage();
                }

            }
        });
    }

    private void getData(String chatId) {

        FirebaseDatabase.getInstance().getReference("Chats")
                .child(chatId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MessageModel data = dataSnapshot.getValue(MessageModel.class);
                            list.add(0,data);

                        }
                        adapter = new MessageAdapter(MessageActivity.this, list);
                        adapter.notifyDataSetChanged();

                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MessageActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendMessage() {

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            String senderId  = task.getResult().getString("phone");
                            String receiverId = getIntent().getStringExtra("userId");
                            String chatId = receiverId + senderId;

                            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            String currentTime = new SimpleDateFormat("HH:mm a", Locale.getDefault()).format(new Date());

                            Map<String, Object> userMsg = new HashMap<>();
                            userMsg.put("Message",_messageEdt.getText().toString());
                            userMsg.put("SenderId", senderId);
                            userMsg.put("CurrentTime", currentTime);
                            userMsg.put("CurrentDate", currentDate);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);
                            reference.child(reference.push().getKey()).setValue(userMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        _messageEdt.setText(null);
                                        Toast.makeText(MessageActivity.this, "Message Send Successfully", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(MessageActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(MessageActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}