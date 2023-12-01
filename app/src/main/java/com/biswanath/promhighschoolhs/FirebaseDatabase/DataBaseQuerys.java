package com.biswanath.promhighschoolhs.FirebaseDatabase;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.biswanath.promhighschoolhs.NotificationPojo.PushNotification;
import com.biswanath.promhighschoolhs.api.ApiUtilities;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataBaseQuerys {

    public static String Image;
    public static String phone;
    public static String Name;
    public static String roll;
    public static String section;
    public static String whichClass;
    public static long PromCoin;
    //public static String Verified;
    public static boolean Verified;

    //InterstitialAd
    public static InterstitialAd mInterstitialAd;
    public static void loadInterstitialAd(Context context){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context,"ca-app-pub-5082541445865756/4621130698", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        mInterstitialAd = interstitialAd;

                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        loadInterstitialAd(context);
                    }
                });
    }
    //InterstitialAd

    //RewardedAd
    public static RewardedAd mRewardedAd;
    public static void loadRewardedAd(Context context){

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, "ca-app-pub-5082541445865756/2125176907",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        loadRewardedAd(context);
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        mRewardedAd = rewardedAd;
                    }
                });


    }
    //RewardedAd




    //Create Notification
    public static final String BASE_URL = "https://fcm.googleapis.com";
    public static final String SERVER_KEY = "AAAA1J9fGiY:APA91bHjGu7DFmzlehuSNPowCpbxZW75ZPXDCqL3rZrcAajXh_RWP51pmkH3Tx3km_U1jcp9Px_MmbE_UEUKNqCnN3qgKlTkaRnGjtNwbMhHwUAEw-BIz5t1-hys2sS9SFAY5SY2XGvN";
    public static final String CONTENT_TYPE ="application/json";
    public static final String TOPIC ="/topics/papayacoders";

    public static void sendNotification(PushNotification notification, Context context) {

        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }







}


