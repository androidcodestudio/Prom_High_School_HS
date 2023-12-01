package com.biswanath.promhighschoolhs.ClassFive;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.biswanath.promhighschoolhs.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassFiveConceptActivity extends AppCompatActivity {

    private RecyclerView ConceptRecyclerView;
    private DatabaseReference reference;
    private List<ClassFiveConceptPojo> list;
    private ClassFiveConceptAdapter adapter;
    private AdView mAdView;
    private ImageView _back_buttonConcept;
    private Window _window;

    private String categoryName;
    private int set;

    private LottieAnimationView _empty_chapter;
    private TextView no_chapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_five_concept);

        progressDialog = new ProgressDialog(ClassFiveConceptActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Loading Concept....");

        _empty_chapter = (com.airbnb.lottie.LottieAnimationView) findViewById(R.id.animation);
        no_chapter = findViewById(R.id.no_order_text);

        _window=this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        reference = FirebaseDatabase.getInstance().getReference();
        categoryName = getIntent().getStringExtra("category");
        set = getIntent().getIntExtra("setNo", 1);

        ConceptRecyclerView = findViewById(R.id.concept_item_recycler_view);
        _back_buttonConcept = findViewById(R.id.back_buttonConcept);

        _back_buttonConcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        ConceptRecyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();

        adapter = new ClassFiveConceptAdapter(ClassFiveConceptActivity.this,list, categoryName);

        ConceptRecyclerView.setAdapter(adapter);

        //banner ad
        mAdView = findViewById(R.id.adViewConcept);
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

        getData(categoryName);
    }


    private void getData(String categoryName) {
       progressDialog.show();
        reference.child("CLASS_FIVE_CONCEPT")
                .child(categoryName)
                .child("concept")
                .orderByChild("setNo")
                .equalTo(set).addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String id = dataSnapshot.getKey();
                            String concept = dataSnapshot.child("concept").getValue().toString();
                            String url = dataSnapshot.child("url").getValue().toString();
                            int set = Integer.parseInt(dataSnapshot.child("setNo").getValue().toString());
                            list.add(new ClassFiveConceptPojo(id,concept,url,set));
                        }
                        progressDialog.dismiss();
                        if (list.size() == 0){
                            _empty_chapter.setVisibility(View.VISIBLE);
                            ConceptRecyclerView.setVisibility(View.GONE);
                            no_chapter.setVisibility(View.VISIBLE);
                        }else{
                            _empty_chapter.setVisibility(View.GONE);
                            no_chapter.setVisibility(View.GONE);
                            ConceptRecyclerView.setVisibility(View.VISIBLE);

                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ClassFiveConceptActivity.this, "error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                });

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }
}