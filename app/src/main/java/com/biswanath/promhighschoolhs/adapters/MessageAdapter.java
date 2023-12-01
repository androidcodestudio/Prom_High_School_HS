package com.biswanath.promhighschoolhs.adapters;




import androidx.appcompat.content.res.AppCompatResources;
import android.content.res.Configuration;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.biswanath.promhighschoolhs.MessagingActivity;
import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.models.MessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



public class MessageAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> msgData;
    Context context;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    final int SENDER_VIEWHOLDER = 0;
    final int RECEIVER_VIEWHOLDER = 1;


    public MessageAdapter(ArrayList<MessageModel> msgData, Context context) {

        this.msgData = msgData;
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {

        if (msgData.get(position).getuId().equals(firebaseAuth.getUid()))
            return SENDER_VIEWHOLDER;
        else
            return RECEIVER_VIEWHOLDER;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEWHOLDER) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_listitem, parent, false);
            return new OutgoingViewholder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_listitem, parent, false);
            return new IncomingViewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getClass() == OutgoingViewholder.class) {
            ((OutgoingViewholder) holder).outgoingMsg.setText(msgData.get(position).getMsgText());

            long time = msgData.get(position).getMsgTime();
            final Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            final String timeString = new SimpleDateFormat("HH:mm").format(cal.getTime());

            ((OutgoingViewholder) holder).outgoingMsgTime.setText(timeString);
        } else {

            ((IncomingViewholder) holder).incomingMsg.setText(msgData.get(position).getMsgText());
            long time = msgData.get(position).getMsgTime();
            final Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            final String timeString = new SimpleDateFormat("HH:mm").format(cal.getTime());

            ((IncomingViewholder) holder).incomingMsgTime.setText(timeString);
        }


    }

    @Override
    public int getItemCount() {
        return msgData.size();
    }

    public class OutgoingViewholder extends RecyclerView.ViewHolder {
        ConstraintLayout _parentViewgroup;
        TextView outgoingMsg, outgoingMsgTime;



        public OutgoingViewholder(@NonNull View itemView) {
            super(itemView);
            _parentViewgroup = itemView.findViewById(R.id.parent_viewgroup_receiver);
            outgoingMsg = itemView.findViewById(R.id.outgoing_msg);
            outgoingMsgTime = itemView.findViewById(R.id.outgoing_msg_time);

            switch (itemView.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    _parentViewgroup.setBackground(AppCompatResources.getDrawable(itemView.getContext(),R.drawable.chat_bubble_outgoing_dark));
                    outgoingMsg.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    _parentViewgroup.setBackground(AppCompatResources.getDrawable(itemView.getContext(),R.drawable.chat_bubble_outgoing));
                    break;
            }
        }
    }

    public class IncomingViewholder extends RecyclerView.ViewHolder {

        TextView incomingMsg, incomingMsgTime;
        ConstraintLayout _parentViewgroup;

        public IncomingViewholder(@NonNull View itemView) {
            super(itemView);

            _parentViewgroup = itemView.findViewById(R.id.parent_viewgroup_receiver);
            incomingMsg = itemView.findViewById(R.id.incoming_msg);
            incomingMsgTime = itemView.findViewById(R.id.incoming_msg_time);

            switch (itemView.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    _parentViewgroup.setBackground(AppCompatResources.getDrawable(itemView.getContext(),R.drawable.chat_bubble_incoming_dark));
                    incomingMsg.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    _parentViewgroup.setBackground(AppCompatResources.getDrawable(itemView.getContext(),R.drawable.chat_bubble_incoming));
                    break;
            }
        }
    }

}
