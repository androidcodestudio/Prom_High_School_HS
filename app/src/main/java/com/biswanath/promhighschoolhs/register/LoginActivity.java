package com.biswanath.promhighschoolhs.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.biswanath.promhighschoolhs.MainActivity;
import com.biswanath.promhighschoolhs.R;
import com.biswanath.promhighschoolhs.StudentZone.MessageActivity;
import com.biswanath.promhighschoolhs.intro.IntroActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends AppCompatActivity {
    private TextInputEditText _nameEditText,_mobileEditText,_rollEditText;
    private ProgressBar _progressBar;
    private Button _btnVerify,_forReview,_googleSignin;
    private FirebaseAuth _firebaseAuth;
    private Window _window;

    private Spinner section_category,class_category;
    private ImageView selectImage;
    private CircleImageView galleryImageView;
    private String category,category1;

    private Bitmap bitmap;
    private final int REQ = 1;


    private DatabaseReference databaseReference,dbRef;
    private StorageReference storageReference;
    String downloadUrl="";
    private ProgressDialog progressDialog;
    private FirebaseFirestore _db;

    private FirebaseAuth myAuth;
    FirebaseDatabase firebaseDatabase;
    ActivityResultLauncher<Intent> activityResultLauncher;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);


        myAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        initializeView();

        _window=this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on


        String[] item = new String[]{"Class","V","Vi","Vii","Viii","IX","X","Xi","Xii"};
        class_category.setAdapter(new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item,item));

        class_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category1 = class_category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] items = new String[]{"Section","A","B","C","D","E","F"};
        section_category.setAdapter(new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item,items));

        section_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = section_category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(LoginActivity.this);
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, REQ);
            }
        });

        _googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _nameEditText.setError(null);
                _mobileEditText.setError(null);
                _rollEditText.setError(null);


                if (bitmap == null) {
                    Toast.makeText(LoginActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_nameEditText.getText().toString().isEmpty()) {
                    _nameEditText.setError("required");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_mobileEditText.getText().toString().isEmpty()) {
                    _mobileEditText.setError("required");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_mobileEditText.getText().toString().length() != 10) {
                    _mobileEditText.setError("please enter a valid phone number");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_rollEditText.getText().toString().isEmpty()) {
                    _rollEditText.setError("required");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (category.equals("Section")) {
                    Toast.makeText(LoginActivity.this, "Please Select Section", Toast.LENGTH_SHORT).show();
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (category1.equals("Class")) {
                    Toast.makeText(LoginActivity.this, "Please Select Class", Toast.LENGTH_SHORT).show();
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }
                uploadImageForGoogleLogin();
                Intent signInIntent  = mGoogleSignInClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);

            }
        });

        _btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _nameEditText.setError(null);
                _mobileEditText.setError(null);
                _rollEditText.setError(null);


                if (bitmap == null) {
                    Toast.makeText(LoginActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_nameEditText.getText().toString().isEmpty()) {
                    _nameEditText.setError("required");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_mobileEditText.getText().toString().isEmpty()) {
                    _mobileEditText.setError("required");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_mobileEditText.getText().toString().length() != 10) {
                    _mobileEditText.setError("please enter a valid phone number");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_rollEditText.getText().toString().isEmpty()) {
                    _rollEditText.setError("required");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (category.equals("Section")) {
                    Toast.makeText(LoginActivity.this, "Please Select Section", Toast.LENGTH_SHORT).show();
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (category1.equals("Class")) {
                    Toast.makeText(LoginActivity.this, "Please Select Class", Toast.LENGTH_SHORT).show();
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                uploadImage();

            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {



                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());

                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                //activitySignupBinding.progressBar.setVisibility(View.GONE);
            }
        });

//        _forReview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent otp_intent = new Intent(LoginActivity.this, MainActivity.class);
//                otp_intent.putExtra("Class","V");
////                otp_intent.putExtra("Test","ForReview");
//                otp_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
//                startActivity(otp_intent);
//                finish();
//            }
//        });
//        _forReview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                _nameEditText.setError(null);
//                _mobileEditText.setError(null);
//                _rollEditText.setError(null);
//
//                Map<String, Object> user = new HashMap<>();
//                user.put("Image","https://firebasestorage.googleapis.com/v0/b/exam-979e3.appspot.com/o/categories%2F%5BB%405034fjpg?alt=media&token=c37dbe5d-c488-4725-bbb3-e37ef78b76af");
//                user.put("Name", "Tester");
//                user.put("phone", "+919735672070");
//                user.put("Roll", "1");
//                user.put("Section", "A");
//                user.put("Class", "V");
//
//                // Add a new document with a generated ID
//                _db.collection("Demo").document("kLK9gbSyaodhdPA5F1ofNPOA6LH3")
//                        .set(user)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Intent otp_intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    otp_intent.putExtra("UID","kLK9gbSyaodhdPA5F1ofNPOA6LH3");
//
//                                    otp_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
//                                    startActivity(otp_intent);
//                                    finish();
//
//                                } else {
//                                    Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//
//            }
//        });
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        myAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String id =  task.getResult().getUser().getUid();

                            //To not override default user values in DB when signing again with google
                            firebaseDatabase.getReference().child("Users")
                                    .child(id)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {

                                    if(!dataSnapshot.hasChild("userName")){

                                        String defaultUserName = task.getResult().getUser().getEmail();
                                        String about = "Online";
                                        String email = task.getResult().getUser().getEmail();
                                        String photoUrl = task.getResult().getUser().getPhotoUrl().toString();

                                        String image = downloadUrl;
                                        String name = _nameEditText.getText().toString();
                                        String promCoin = "";
                                        String roll = _rollEditText.getText().toString();
                                        String section = category;
                                        String studentClass = category1;
                                        String phone = _mobileEditText.getText().toString();
                                        String myUid =  task.getResult().getUser().getUid();



                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(LoginActivity.this, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }

                                                        // Get new FCM registration token
                                                       String token = task.getResult();

                                                        UserModel userModel = new UserModel(
                                                                defaultUserName.substring(0, defaultUserName.indexOf('@'))
                                                                , email
                                                                , "null"
                                                                , photoUrl
                                                                , about
                                                                ,image
                                                                ,name
                                                                ,promCoin
                                                                ,roll
                                                                ,section
                                                                ,studentClass
                                                                ,phone
                                                        ,myUid);



                                                        userModel.setToken(token);
                                                        firebaseDatabase.getReference().child("Users").child(id).setValue(userModel);


                                                    }
                                                });

                                    }else{

                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(LoginActivity.this, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }

                                                        // Get new FCM registration token
                                                        String Newtoken = task.getResult();

                                                        firebaseDatabase.getReference("Users")
                                                                .child(id)
                                                                .child("token")
                                                                .setValue(Newtoken);


                                                    }
                                                });



                                    }
                                }
                            });

                            progressDialog.dismiss();
                            Map<String, Object> user = new HashMap<>();
                            user.put("Image",downloadUrl);
                            user.put("Name", _nameEditText.getText().toString());
                            user.put("phone", _mobileEditText.getText().toString());
                            user.put("Roll", _rollEditText.getText().toString());
                            user.put("Section", category);
                            user.put("Class", category1);
                            user.put("Prom_coin", 0);

                            // Add a new document with a generated ID
                            _db.collection("USERS").document(_firebaseAuth.getUid())
                                    .set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent otp_intent = new Intent(LoginActivity.this, IntroActivity.class);
                                                otp_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                                startActivity(otp_intent);
                                                finish();

                                            } else {
                                                Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage()+"", Toast.LENGTH_SHORT).show();
                            Log.d("TAG2", "signInWithCredential:failure", task.getException());
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void initializeView() {
        section_category = findViewById(R.id.section_category);
        class_category = findViewById(R.id.class_category);
        selectImage = findViewById(R.id.editPhotoIcon);
        galleryImageView = findViewById(R.id.profile_img);

        _nameEditText = findViewById(R.id.edtSignupName);
        _mobileEditText = findViewById(R.id.edtSignupMobile);
        _rollEditText = findViewById(R.id.edtSignupRoll);
        _progressBar = findViewById(R.id.login_progressbar);
        _btnVerify = findViewById(R.id.btnVerify);
        _forReview = findViewById(R.id.btnReview);
        _googleSignin = findViewById(R.id.google_signin);
        _db = FirebaseFirestore.getInstance();
    }
    private void go_to_otp(){
        Intent otp_intent = new Intent(LoginActivity.this, OtpActivity.class);
        otp_intent.putExtra("Person",downloadUrl);
        otp_intent.putExtra("Name",_nameEditText.getText().toString());
        otp_intent.putExtra("Phone",_mobileEditText.getText().toString());
        otp_intent.putExtra("Roll",_rollEditText.getText().toString());
        otp_intent.putExtra("Section",category);
        otp_intent.putExtra("Class",category1);
        otp_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(otp_intent);
        finish();
    }

    private void go_to_main(){
        Intent otp_intent = new Intent(LoginActivity.this, MainActivity.class);
        otp_intent.putExtra("Person",downloadUrl);
        otp_intent.putExtra("Name",_nameEditText.getText().toString());
        otp_intent.putExtra("Phone",_mobileEditText.getText().toString());
        otp_intent.putExtra("Roll",_rollEditText.getText().toString());
        otp_intent.putExtra("Section",category);
        otp_intent.putExtra("Class",category1);
        otp_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(otp_intent);
        finish();
    }

    private void uploadImage() {
        progressDialog.setMessage("verifying...");
        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;

        filePath= storageReference.child("UserProfile").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(LoginActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    //uploadData();
                                    go_to_otp();
                                }
                            });
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void uploadImageForGoogleLogin() {
        progressDialog.setMessage("verifying...");
        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;

        filePath= storageReference.child("UserProfile").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(LoginActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    //uploadData();
                                    //go_to_otp();
                                }
                            });
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);

                }catch (IOException e){
                    e.printStackTrace();
                }
                Glide.with(this).load(resultUri).into(galleryImageView);
                //Picasso.with(this).load(resultUri).into(galleryImageView);
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}
