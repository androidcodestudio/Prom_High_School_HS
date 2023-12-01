package com.biswanath.promhighschoolhs.splash;

import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.loadInterstitialAd;
import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.Verified;
import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.loadRewardedAd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.biswanath.promhighschoolhs.AdminLogin.AdminLoginActivity;
import com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys;
import com.biswanath.promhighschoolhs.MainActivity;
import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.register.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth _firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        loadInterstitialAd(SplashActivity.this);
        loadRewardedAd(SplashActivity.this);


        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        _firebaseAuth = FirebaseAuth.getInstance();



        FirebaseFirestore.getInstance().collection("isVerified")
                .document("lxKrVdCC4RvED2i5pwDU")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            //DataBaseQuerys.Verified = Boolean.parseBoolean(task.getResult().getString("Verified"));
                            Verified = (boolean) task.getResult().get("Verified");

                            if (DataBaseQuerys.Verified) {
                                FirebaseUser currentUser = _firebaseAuth.getCurrentUser();
                                if (currentUser == null) {

                                    Intent RegisterIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(RegisterIntent);
                                    finish();

                                } else {
                                    FirebaseFirestore.getInstance().collection("USERS")
                                            .document(currentUser.getUid()).update("LAST_SEEN", FieldValue.serverTimestamp())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                        intent.putExtra("Test","LoginPurpose");
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Intent RegisterIntent = new Intent(SplashActivity.this, AdminLoginActivity.class);
                                startActivity(RegisterIntent);
                                finish();
                            }



                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}