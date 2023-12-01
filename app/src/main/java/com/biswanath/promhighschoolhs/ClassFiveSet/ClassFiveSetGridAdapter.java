package com.biswanath.promhighschoolhs.ClassFiveSet;


import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.Verified;
import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.loadInterstitialAd;
import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.mInterstitialAd;
import static com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys.whichClass;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.biswanath.promhighschoolhs.AdminLogin.AdminLoginActivity;
import com.biswanath.promhighschoolhs.ClassFiveQuestion.ClassFiveQuestionActivity;
import com.biswanath.promhighschoolhs.FirebaseDatabase.DataBaseQuerys;
import com.biswanath.promhighschoolhs.MainActivity;
import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.register.LoginActivity;
import com.biswanath.promhighschoolhs.splash.SplashActivity;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


public class ClassFiveSetGridAdapter extends BaseAdapter {
    private int sets = 0;
    private String category;

    public ClassFiveSetGridAdapter(int sets, String category) {
        this.sets = sets;
        this.category = category;
    }

    @Override
    public int getCount() {
        return sets;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView==null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_item_grid_view,parent,false);
        }else
        {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(parent.getContext());
                progressDialog.setMessage("Getting Mock-test...");
                progressDialog.show();

                 FirebaseAuth _firebaseAuth;
                _firebaseAuth = FirebaseAuth.getInstance();

                loadInterstitialAd(parent.getContext());

                FirebaseUser currentUser = _firebaseAuth.getCurrentUser();
                if (currentUser == null) {

                    progressDialog.dismiss();
                    new AlertDialog.Builder(parent.getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Login Required !")
                            .setMessage("You Have to First Login")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent RegisterIntent = new Intent(parent.getContext(), LoginActivity.class);
                                    parent.getContext().startActivity(RegisterIntent);
                                    ((Activity)parent.getContext()).finish();
                                }

                            })
                            .setNegativeButton("Not Now", null)
                            .show();


                } else {
                    FirebaseFirestore.getInstance().collection("USERS")
                            .document(FirebaseAuth.getInstance().getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();

                                        whichClass = task.getResult().getString("Class");

                                        if (mInterstitialAd != null){
                                            mInterstitialAd.show((Activity) parent.getContext());
                                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                                @Override
                                                public void onAdDismissedFullScreenContent() {
                                                    super.onAdDismissedFullScreenContent();

                                                    switch (whichClass) {
                                                        case "V":
                                                            Intent questionintent = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                            questionintent.putExtra("setName","CLASS_FIVE_SETS");
                                                            questionintent.putExtra("category",category);
                                                            questionintent.putExtra("setNo",position+1);
                                                            parent.getContext().startActivity(questionintent);
                                                            break;
                                                        case "Vi":
                                                            Intent questionSix = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                            questionSix.putExtra("setName","CLASS_SIX_SETS");
                                                            questionSix.putExtra("category",category);
                                                            questionSix.putExtra("setNo",position+1);
                                                            parent.getContext().startActivity(questionSix);
                                                            break;
                                                        case "Vii":
                                                            Intent questionSeven = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                            questionSeven.putExtra("setName","CLASS_SEVEN_SETS");
                                                            questionSeven.putExtra("category",category);
                                                            questionSeven.putExtra("setNo",position+1);
                                                            parent.getContext().startActivity(questionSeven);
                                                            break;
                                                        case "Viii":
                                                            Intent questionEight = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                            questionEight.putExtra("setName","CLASS_EIGHT_SETS");
                                                            questionEight.putExtra("category",category);
                                                            questionEight.putExtra("setNo",position+1);
                                                            parent.getContext().startActivity(questionEight);
                                                            break;
                                                        case "IX":
                                                            Intent questionNine = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                            questionNine.putExtra("setName","CLASS_NINE_SETS");
                                                            questionNine.putExtra("category",category);
                                                            questionNine.putExtra("setNo",position+1);
                                                            parent.getContext().startActivity(questionNine);
                                                            break;
                                                        case "X":
                                                            Intent questionTen = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                            questionTen.putExtra("setName","CLASS_TEN_SETS");
                                                            questionTen.putExtra("category",category);
                                                            questionTen.putExtra("setNo",position+1);
                                                            parent.getContext().startActivity(questionTen);
                                                            break;
                                                        case "Xi":
                                                            Intent questionEleven = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                            questionEleven.putExtra("setName","CLASS_ELEVEN_SETS");
                                                            questionEleven.putExtra("category",category);
                                                            questionEleven.putExtra("setNo",position+1);
                                                            parent.getContext().startActivity(questionEleven);
                                                            break;
                                                        case "Xii":
                                                            Intent questionTwelve = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                            questionTwelve.putExtra("setName","CLASS_TWELVE_SETS");
                                                            questionTwelve.putExtra("category",category);
                                                            questionTwelve.putExtra("setNo",position+1);
                                                            parent.getContext().startActivity(questionTwelve);
                                                            break;
                                                    }
                                                }
                                            });
                                        }else {

                                            switch (whichClass) {
                                                case "V":
                                                    Intent questionintent = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                    questionintent.putExtra("setName","CLASS_FIVE_SETS");
                                                    questionintent.putExtra("category",category);
                                                    questionintent.putExtra("setNo",position+1);
                                                    parent.getContext().startActivity(questionintent);
                                                    break;
                                                case "Vi":
                                                    Intent questionSix = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                    questionSix.putExtra("setName","CLASS_SIX_SETS");
                                                    questionSix.putExtra("category",category);
                                                    questionSix.putExtra("setNo",position+1);
                                                    parent.getContext().startActivity(questionSix);
                                                    break;
                                                case "Vii":
                                                    Intent questionSeven = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                    questionSeven.putExtra("setName","CLASS_SEVEN_SETS");
                                                    questionSeven.putExtra("category",category);
                                                    questionSeven.putExtra("setNo",position+1);
                                                    parent.getContext().startActivity(questionSeven);
                                                    break;
                                                case "Viii":
                                                    Intent questionEight = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                    questionEight.putExtra("setName","CLASS_EIGHT_SETS");
                                                    questionEight.putExtra("category",category);
                                                    questionEight.putExtra("setNo",position+1);
                                                    parent.getContext().startActivity(questionEight);
                                                    break;
                                                case "IX":
                                                    Intent questionNine = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                    questionNine.putExtra("setName","CLASS_NINE_SETS");
                                                    questionNine.putExtra("category",category);
                                                    questionNine.putExtra("setNo",position+1);
                                                    parent.getContext().startActivity(questionNine);
                                                    break;
                                                case "X":
                                                    Intent questionTen = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                    questionTen.putExtra("setName","CLASS_TEN_SETS");
                                                    questionTen.putExtra("category",category);
                                                    questionTen.putExtra("setNo",position+1);
                                                    parent.getContext().startActivity(questionTen);
                                                    break;
                                                case "Xi":
                                                    Intent questionEleven = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                    questionEleven.putExtra("setName","CLASS_ELEVEN_SETS");
                                                    questionEleven.putExtra("category",category);
                                                    questionEleven.putExtra("setNo",position+1);
                                                    parent.getContext().startActivity(questionEleven);
                                                    break;
                                                case "Xii":
                                                    Intent questionTwelve = new Intent(parent.getContext(), ClassFiveQuestionActivity.class);
                                                    questionTwelve.putExtra("setName","CLASS_TWELVE_SETS");
                                                    questionTwelve.putExtra("category",category);
                                                    questionTwelve.putExtra("setNo",position+1);
                                                    parent.getContext().startActivity(questionTwelve);
                                                    break;
                                            }
                                        }

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(parent.getContext(), error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }






            }
        });

        ((TextView)view.findViewById(R.id.set_number)).setText(category+" Mock Test "+String.valueOf(position+1));
        return view;
    }
}
