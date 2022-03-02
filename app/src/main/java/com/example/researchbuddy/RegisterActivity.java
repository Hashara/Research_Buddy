package com.example.researchbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private TextInputLayout txtLayoutFirstName;
    private TextInputLayout txtLayoutLastName;
    private TextInputLayout txtLayoutEmail;
    private RadioGroup radioGroupSelectRole;
    private Button btnRegister;
    private RelativeLayout parent;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

        validateData();

        btnOnClick();
    }

    private void initViews() {
        Log.d(TAG, "initViews: started");

        txtLayoutFirstName = findViewById(R.id.txtLayoutFirstName);
        txtLayoutLastName = findViewById(R.id.txtLayoutLastName);
        txtLayoutEmail = findViewById(R.id.txtLayoutEmail);

        radioGroupSelectRole = findViewById(R.id.radioGroupSelectRole);

        btnRegister = findViewById(R.id.btnRegister);

        parent = findViewById(R.id.parent);
    }


    private void initRegister() {
        Log.d(TAG, "initRegister: started");

    }

    private void validateData() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(txtLayoutFirstName.getEditText(), RegexTemplate.NOT_EMPTY, getString(R.string.error_msg_first_name_empty));
        awesomeValidation.addValidation(txtLayoutLastName.getEditText(), RegexTemplate.NOT_EMPTY, getString(R.string.error_msg_last_name_empty));
        awesomeValidation.addValidation(txtLayoutEmail.getEditText(), Patterns.EMAIL_ADDRESS,getString(R.string.error_msg_email_empty));

    }

    private void btnOnClick(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()){
                    Toast.makeText(RegisterActivity.this, txtLayoutFirstName.getEditText().getText(), Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(RegisterActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}