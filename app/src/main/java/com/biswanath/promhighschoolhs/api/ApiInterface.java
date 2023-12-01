package com.biswanath.promhighschoolhs.api;




import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.CONTENT_TYPE;
import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.SERVER_KEY;

import com.biswanath.promhighschoolhs.NotificationPojo.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ApiInterface {
    @Headers({"Authorization: key="+SERVER_KEY,"Content-Type:"+CONTENT_TYPE})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification notification);
}
