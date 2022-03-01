package com.example.researchbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private TextInputLayout txtLayoutFirstName;
    private TextInputLayout txtLayoutLastName;
    private TextInputLayout txtLayoutEmail;
    private RadioGroup radioGroupSelectRole;
    private Button btnRegister;
    private RelativeLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, txtLayoutFirstName.getEditText().getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(){
        Log.d(TAG, "initViews: started");

        txtLayoutFirstName = findViewById(R.id.txtLayoutFirstName);
        txtLayoutLastName = findViewById(R.id.txtLayoutLastName);
        txtLayoutEmail = findViewById(R.id.txtLayoutEmail);

        radioGroupSelectRole = findViewById(R.id.radioGroupSelectRole);

        btnRegister = findViewById(R.id.btnRegister);

        parent = findViewById(R.id.parent);
    }



    private void initRegister(){
        Log.d(TAG, "initRegister: started");

    }

    private boolean validateData(){
        return false;
    }
}