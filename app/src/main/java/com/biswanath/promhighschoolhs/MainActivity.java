package com.biswanath.promhighschoolhs;


import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.TOPIC;
import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.loadInterstitialAd;
import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.mInterstitialAd;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private Window _window;
    private ImageView logo;
    private FirebaseAuth _firebaseAuth;
    private Intent intent;
    private AdView mAdView;
    private LinearLayout frame, frame1, frame2, frame3, frame4, frame5, frame6, frame7;
    private static final int MY_REQUEST_CODE = 100;

    private String _uid;

    private TextView _coin;

    private DatabaseReference mdatabse;
    private String mvname="XX";
    FirebaseUser currentUser;



//    private Dialog _loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadInterstitialAd(MainActivity.this);

        askNotificationPermission();
        checkForAppUpdate();
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
        _firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.titles_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);//for red colored toolbar title
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        _window = this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        final Dialog notificationDialog = new Dialog(this);
        notificationDialog.setContentView(R.layout.empty_dialog);
        notificationDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        notificationDialog.setCancelable(true);

        TextView _earnCoin = notificationDialog.findViewById(R.id.earnCoin);

        _earnCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 11/02/23 get rewarded video
                notificationDialog.dismiss();
            }
        });


        logo = findViewById(R.id.logo);
        _coin = findViewById(R.id.coin);

        _coin.setText("xxxx");

        mdatabse= FirebaseDatabase.getInstance().getReference();

        mdatabse.child("HomeNotification").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    mvname=dataSnapshot.child("Notification").getValue().toString();
                   // boolean notification = dataSnapshot.child("uid").getValue();

                    if (mvname.equals("on")){
                        notificationDialog.show();
                    }else {
                        notificationDialog.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frame = findViewById(R.id.frams);
        frame1 = findViewById(R.id.frams_two);
        frame2 = findViewById(R.id.frams_three);
        frame3 = findViewById(R.id.frams_four);
        frame4 = findViewById(R.id.frams_five);
        frame5 = findViewById(R.id.frams_six);
        frame6 = findViewById(R.id.frams_seven);
        frame7 = findViewById(R.id.frams_eight);

        currentUser = _firebaseAuth.getCurrentUser();

        String classes = getIntent().getStringExtra("Class");
        String from = getIntent().getStringExtra("Test");

        ShowInterstitialAd();




        if (from.equals("LoginPurpose")) {
            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                DataBaseQuerys.whichClass = task.getResult().getString("Class");
                                DataBaseQuerys.PromCoin = (long) task.getResult().get("Prom_coin");

                                _coin.setText(String.format(String.valueOf(DataBaseQuerys.PromCoin)));

                                switch (DataBaseQuerys.whichClass) {
                                    case "V":
                                        frame.setVisibility(View.VISIBLE);
                                        navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout);
                                        NavigationUI.setupWithNavController(bottomNavigationView, navController);
                                        //NavigationUI.onNavDestinationSelected()
                                        break;
                                    case "Vi":
                                        frame1.setVisibility(View.VISIBLE);
                                        navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout2);
                                        NavigationUI.setupWithNavController(bottomNavigationView, navController);
                                        break;
                                    case "Vii":
                                        frame2.setVisibility(View.VISIBLE);
                                        navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout3);
                                        NavigationUI.setupWithNavController(bottomNavigationView, navController);
                                        break;
                                    case "Viii":
                                        frame3.setVisibility(View.VISIBLE);
                                        navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout4);
                                        NavigationUI.setupWithNavController(bottomNavigationView, navController);
                                        break;
                                    case "IX":
                                        frame4.setVisibility(View.VISIBLE);
                                        navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout5);
                                        NavigationUI.setupWithNavController(bottomNavigationView, navController);
                                        break;
                                    case "X":
                                        frame5.setVisibility(View.VISIBLE);
                                        navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout6);
                                        NavigationUI.setupWithNavController(bottomNavigationView, navController);
                                        break;
                                    case "Xi":
                                        frame6.setVisibility(View.VISIBLE);
                                        navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout7);
                                        NavigationUI.setupWithNavController(bottomNavigationView, navController);
                                        break;
                                    case "Xii":
                                        frame7.setVisibility(View.VISIBLE);
                                        navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout8);
                                        NavigationUI.setupWithNavController(bottomNavigationView, navController);
                                        break;
                                }

                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        if (from.equals("ForReview")) {
            switch (classes) {
                case "V":
                    frame.setVisibility(View.VISIBLE);
                    navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout);
                    NavigationUI.setupWithNavController(bottomNavigationView, navController);
                    break;
                case "Vi":
                    frame1.setVisibility(View.VISIBLE);
                    navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout2);
                    NavigationUI.setupWithNavController(bottomNavigationView, navController);
                    break;
                case "Vii":
                    frame2.setVisibility(View.VISIBLE);
                    navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout3);
                    NavigationUI.setupWithNavController(bottomNavigationView, navController);
                    break;
                case "Viii":
                    frame3.setVisibility(View.VISIBLE);
                    navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout4);
                    NavigationUI.setupWithNavController(bottomNavigationView, navController);
                    break;
                case "IX":
                    frame4.setVisibility(View.VISIBLE);
                    navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout5);
                    NavigationUI.setupWithNavController(bottomNavigationView, navController);
                    break;
                case "X":
                    frame5.setVisibility(View.VISIBLE);
                    navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout6);
                    NavigationUI.setupWithNavController(bottomNavigationView, navController);
                    break;
                case "Xi":
                    frame6.setVisibility(View.VISIBLE);
                    navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout7);
                    NavigationUI.setupWithNavController(bottomNavigationView, navController);
                    break;
                case "Xii":
                    frame7.setVisibility(View.VISIBLE);
                    navController = Navigation.findNavController(MainActivity.this, R.id.frame_layout8);
                    NavigationUI.setupWithNavController(bottomNavigationView, navController);
                    break;
            }
        }

        //banner ad
        mAdView = findViewById(R.id.adView_bottom);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {


                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }

    private void ShowInterstitialAd(){
        if (mInterstitialAd != null){
            mInterstitialAd.show(MainActivity.this);
        }
    }

    boolean back_agin = false;

    @Override
    public void onBackPressed() {
        if (back_agin) {
            super.onBackPressed();
        } else {
            back_agin = true;
            ConstraintLayout relativeLayout = (ConstraintLayout) findViewById(R.id.main);
            Snackbar.make(relativeLayout, "Press Again To Exit ! ", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    back_agin = false;
                }
            }, 2000);

        }

    }

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.


                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Toast.makeText(this, "if not allow notification you will not show notifications", Toast.LENGTH_SHORT).show();
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // FCM SDK (and your app) can post notifications.

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

    }

    private void checkForAppUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Update flow failed! Result code: " + resultCode, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null){
            _coin.setText("xxxx");
        }else{
            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                DataBaseQuerys.PromCoin = (long) task.getResult().get("Prom_coin");
                                _coin.setText(String.format(String.valueOf(DataBaseQuerys.PromCoin)));

                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }
}