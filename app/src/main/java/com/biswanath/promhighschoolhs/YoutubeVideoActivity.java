package com.biswanath.promhighschoolhs;

import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.loadRewardedAd;
import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.mRewardedAd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class YoutubeVideoActivity extends AppCompatActivity {

    WebView webView;
    String htmlCode;
    String videoId;
    private ProgressDialog progressDialog;
    WebSettings settings;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_video);
        progressDialog = new ProgressDialog(YoutubeVideoActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Loading Class....");

        webView = findViewById(R.id.webView);
        settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);


        videoId = getIntent().getStringExtra("videoUrl");
        htmlCode = "<html><body style='margin:0;padding:0;'><iframe allow='fullscreen;' id='player' type='text/html' width='100%' height='100%' src='https://www.youtube.com/embed/" + videoId + "?enablejsapi=1' frameborder='0'></iframe></body></html>";


        loadRewardedAd(YoutubeVideoActivity.this);
        if (mRewardedAd!=null){

            mRewardedAd.show((Activity) YoutubeVideoActivity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {


                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        int rewardAmount = rewardItem.getAmount();
                                        DataBaseQuerys.PromCoin = (long) task.getResult().get("Prom_coin");
                                        long coinValue = DataBaseQuerys.PromCoin + rewardAmount;

                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .update("Prom_coin", coinValue)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(YoutubeVideoActivity.this, "You Earn 10 Coin", Toast.LENGTH_SHORT).show();
                                                            //webView.loadDataWithBaseURL(null, htmlCode, "text/html", "UTF-8", null);
                                                        } else {
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(YoutubeVideoActivity.this, error, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(YoutubeVideoActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });




                }
            });
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    progressDialog.show();
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setDomStorageEnabled(true);
                    settings = webView.getSettings();
                    settings.setDomStorageEnabled(true);
                    webView.setWebViewClient(new WebViewClient());
                    webView.setWebChromeClient(new CustomWebChromeClient(YoutubeVideoActivity.this));
                    webView.loadDataWithBaseURL("", htmlCode, "text/html", "UTF-8", "");
                    progressDialog.dismiss();
                }
            });

        }else{
            progressDialog.show();
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            settings = webView.getSettings();
            settings.setDomStorageEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new CustomWebChromeClient(YoutubeVideoActivity.this));
            webView.loadDataWithBaseURL("", htmlCode, "text/html", "UTF-8", "");
            progressDialog.dismiss();
        }

    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}