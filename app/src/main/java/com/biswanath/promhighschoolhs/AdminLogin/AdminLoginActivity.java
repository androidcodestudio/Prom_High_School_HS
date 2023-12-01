package com.biswanath.promhighschoolhs.AdminLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.biswanath.promhighschoolhs.MainActivity;
import com.biswanath.promhighschoolhs.R;


public class AdminLoginActivity extends AppCompatActivity {


    private ProgressBar _progressBar;
    private Button _btnVerify;

    private Spinner class_category;
    private String category1;
    private Window _window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        _window=this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        _progressBar = findViewById(R.id.login_progressbar);
        _btnVerify = findViewById(R.id.btnVerify);

        class_category = findViewById(R.id.class_categorys);

        String[] item = new String[]{"Class","V","Vi","Vii","Viii","IX","X","Xi","Xii"};
        class_category.setAdapter(new ArrayAdapter<String>(AdminLoginActivity.this, android.R.layout.simple_spinner_dropdown_item,item));

        class_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category1 = class_category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (category1.equals("Class")) {
                    Toast.makeText(AdminLoginActivity.this, "Please Select Class", Toast.LENGTH_SHORT).show();
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                go_to_main();

            }
        });

    }

    private void go_to_main(){
        Intent otp_intent = new Intent(AdminLoginActivity.this, MainActivity.class);
        otp_intent.putExtra("Class",category1);
        otp_intent.putExtra("Test","ForReview");
        otp_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(otp_intent);
        finish();
    }
}