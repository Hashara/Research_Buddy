package com.example.researchbuddy.component.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.researchbuddy.R;
import com.example.researchbuddy.component.participant.ParticipantHomeActivity;
import com.example.researchbuddy.component.researcher.ResearcherHomeActivity;
import com.example.researchbuddy.db.UserDocument;
import com.example.researchbuddy.model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";


    private TextInputLayout txtLayoutEmail;
    private TextInputLayout txtLayoutPassword;

    private TextView txtDirectRegister;
    private Button btnLogin;

    private AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        validateData();
        btnOnClick();
        onClickDirectRegister();

    }

    private void initViews() {
        txtLayoutEmail = findViewById(R.id.txtLayoutEmail);
        txtLayoutPassword = findViewById(R.id.txtLayoutPassword);
        txtDirectRegister = findViewById(R.id.txtDirectRegister);

        btnLogin = findViewById(R.id.btnLogin);

    }

    private void onClickDirectRegister() {
        Log.d(TAG, "Directing to register");
        txtDirectRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
    }

    private void validateData() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(txtLayoutEmail.getEditText(), Patterns.EMAIL_ADDRESS, getString(R.string.error_msg_email_empty));
        awesomeValidation.addValidation(txtLayoutPassword.getEditText(), "^(?=.*[0-9])(?=.*[~`!@#$%^&*])[a-zA-Z0-9~`!@#$%^&*]{6,}$", getString(R.string.error_msg_password_constraints));
    }

    private void btnOnClick() {
        Log.d(TAG, "On click login");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    userLogin(txtLayoutEmail.getEditText().getText().toString(),
                            txtLayoutPassword.getEditText().getText().toString());

                }
            }
        });
    }

    private void userLogin(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(LoginActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "Login successfully");
                UserDocument userDocument = new UserDocument();
                userDocument.checkUserRoleAndRedirect(LoginActivity.this);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // todo: add error message in UI
                        Log.d(TAG, "get failed with ", e);
                    }
                });
    }
}