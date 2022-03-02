package com.example.researchbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout txtLayoutEmail;
    private TextInputLayout txtLayoutPassword;

    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        txtLayoutEmail = findViewById(R.id.txtLayoutEmail);
        txtLayoutPassword = findViewById(R.id.txtLayoutPassword);

        btnLogin = findViewById(R.id.btnLogin);


    }
}