package com.biswanath.promhighschoolhs.StudentZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.biswanath.promhighschoolhs.FullImageViewActivity;
import com.biswanath.promhighschoolhs.MessagingActivity;
import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.register.UserModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    Context context;
    ArrayList<UserModel> list;





    public UserAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_items, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.setData(list.get(position).getProfilePic(),list.get(position).getUserName(),list.get(position).getUserId(),list.get(position).getToken(),list.get(position).getUserName(),position);
        String _url = list.get(position).getProfilePic();
        String _name = list.get(position).getName();
        String _id = list.get(position).getMyUid();
        String _token = list.get(position).getToken();
        String _userName = list.get(position).getUserName();
        holder.setData(_url,_name,_id,_token,_userName,position);

        holder._remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView _icon;
        TextView _name;
        Button _connect,_remove;
        FirebaseDatabase _firebaseDatabase;
        FirebaseAuth _firebaseAuth;





        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            _icon = itemView.findViewById(R.id.studentIcon);
            _name = itemView.findViewById(R.id.userName);
            _connect = itemView.findViewById(R.id.connect_friend);
            _remove = itemView.findViewById(R.id.remove_friend);

            _firebaseDatabase = FirebaseDatabase.getInstance();
            _firebaseAuth = FirebaseAuth.getInstance();

        }



        private void setData(final String url,final String name,final String id,final String token,final String userName,int position){
             Glide.with(itemView.getContext()).load(url).into(_icon);
            _name.setText(name);

//            _firebaseDatabase.getReference("Users")
//                    .child(_firebaseAuth.getCurrentUser().getUid())
//                    .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                        @Override
//                        public void onSuccess(DataSnapshot dataSnapshot) {
//
//                            if (!dataSnapshot.hasChild("Contacts")){
//                                _connect.setText("Connect");
//
//                            }else{
//                                _firebaseDatabase.getReference("Users")
//                                        .child(_firebaseAuth.getCurrentUser().getUid())
//                                        .child("Contacts")
//                                        .get()
//                                        .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                                            @Override
//                                            public void onSuccess(DataSnapshot dataSnapshot) {
//                                                for(DataSnapshot e : dataSnapshot.getChildren()){
//                                                    if(e.getKey().equals(id)){
//
//                                                        _connect.setText("Message");
//                                                        _connect.setOnClickListener(new View.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(View v) {
//                                                                Intent intent = new Intent(itemView.getContext(), MessagingActivity.class);
//                                                                intent.putExtra("USERNAME", userName);
//                                                                intent.putExtra("PROFILEIMAGE",url);
//                                                                intent.putExtra("USERID",id);
//                                                                intent.putExtra("TOKEN",token);
//                                                                itemView.getContext().startActivity(intent);
//                                                            }
//                                                        });
//                                                    }else{
//                                                    _connect.setText("Connect");
//
//                                                    }
//                                                }
//
//
//                                            }
//                                        });
//                            }
//
//
//
//                        }
//                    });



//            if (_firebaseAuth.getCurrentUser().getUid().equals(id)){
//                list.remove(position);
//            }



            _icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), FullImageViewActivity.class);
                    intent.putExtra("image",url);
                    itemView.getContext().startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), MessagingActivity.class);
                    intent.putExtra("USERNAME", userName);
                    intent.putExtra("PROFILEIMAGE",url);
                    intent.putExtra("USERID",id);
                    intent.putExtra("TOKEN",token);
                    itemView.getContext().startActivity(intent);
                }
            });

            _connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Contact added", Toast.LENGTH_SHORT).show();

                    _firebaseDatabase.getReference("Users")
                            .child(_firebaseAuth.getCurrentUser().getUid())
                            .child("Contacts")
                            .child(id)
                            .setValue("Chats");

                    _firebaseDatabase.getReference("Users")
                            .child(_firebaseAuth.getUid())
                            .child("Contacts")
                            .child(id)
                            .child("interactionTime")
                            .setValue(new Date().getTime());

                    _firebaseDatabase.getReference("Users")
                            .child(_firebaseAuth.getUid())
                            .child("Contacts")
                            .child(id)
                            .child("recentMessage")
                            .setValue("");

                }
            });



        }
    }
}
