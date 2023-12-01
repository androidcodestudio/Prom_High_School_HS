package com.biswanath.promhighschoolhs.messaging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.splash.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;

public class FirebaseService extends FirebaseMessagingService {
    private final String CHANNEL_ID  ="channel_id";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        if (remoteMessage.getNotification() != null) {
//            Intent intent = new Intent(this, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }

        Intent intent = new Intent(this, SplashActivity.class);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();



        createNotificationChannel(notificationManager);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



        PendingIntent intent1 = PendingIntent.getActivities(this,0,new Intent[]{intent}, PendingIntent.FLAG_IMMUTABLE);
        Notification notification ;

            notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setSmallIcon(R.drawable.prom)
                    .setAutoCancel(true)
                    .setContentIntent(intent1)
                    .build();

        notificationManager.notify(notificationId,notification);
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"channelName",NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My Description");
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
            notificationManager.createNotificationChannel(channel);


    }

}
