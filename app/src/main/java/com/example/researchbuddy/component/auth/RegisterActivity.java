package com.example.researchbuddy.component.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.example.researchbuddy.R;
import com.example.researchbuddy.component.participant.ParticipantHomeActivity;
import com.example.researchbuddy.component.researcher.ResearcherHomeActivity;
import com.example.researchbuddy.db.UserDocument;
import com.example.researchbuddy.model.type.Role;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private TextInputLayout txtLayoutFirstName, txtLayoutLastName, txtLayoutEmail, txtLayoutPassword, txtLayoutConfirmPassword;
    private RadioGroup radioGroupSelectRole;
    private Button btnRegister;
    private RelativeLayout parent;
    private TextView txtDirectLogin;
    private Role role;

    private AwesomeValidation awesomeValidation;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        validateData();
        btnOnClick();
        onClickDirectLogin();
        onRadioButtonClick();
    }

    private void initViews() {
        Log.d(TAG, "initViews: started");

        txtLayoutFirstName = findViewById(R.id.txtLayoutFirstName);
        txtLayoutLastName = findViewById(R.id.txtLayoutLastName);
        txtLayoutEmail = findViewById(R.id.txtLayoutEmail);
        txtLayoutPassword = findViewById(R.id.txtLayoutPassword);
        txtLayoutConfirmPassword = findViewById(R.id.txtLayoutConfirmPassword);

        txtDirectLogin = findViewById(R.id.txtDirectLogin);
        txtDirectLogin.setMovementMethod(LinkMovementMethod.getInstance());

        radioGroupSelectRole = findViewById(R.id.radioGroupSelectRole);

        btnRegister = findViewById(R.id.btnRegister);

        parent = findViewById(R.id.parent);
        auth = FirebaseAuth.getInstance();
        role = Role.RESEARCHER;
    }


    private void onClickDirectLogin() {
        Log.d(TAG, "Directing to login");
        txtDirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
    }

    private void validateData() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(txtLayoutFirstName.getEditText(), RegexTemplate.NOT_EMPTY, getString(R.string.error_msg_first_name_empty));
        awesomeValidation.addValidation(txtLayoutLastName.getEditText(), RegexTemplate.NOT_EMPTY, getString(R.string.error_msg_last_name_empty));
        awesomeValidation.addValidation(txtLayoutEmail.getEditText(), Patterns.EMAIL_ADDRESS, getString(R.string.error_msg_email_empty));
        awesomeValidation.addValidation(txtLayoutPassword.getEditText(), "^(?=.*[0-9])(?=.*[~`!@#$%^&*])[a-zA-Z0-9~`!@#$%^&*]{6,}$", getString(R.string.error_msg_password_constraints));
        awesomeValidation.addValidation(txtLayoutConfirmPassword.getEditText(), new SimpleCustomValidation() {
            @Override
            public boolean compare(String s) {
                return s.equals(txtLayoutPassword.getEditText().getText().toString());
            }
        }, getString(R.string.error_msg_password_not_match));

    }

    private void btnOnClick() {
        Log.d(TAG, "On click register");
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    userRegister(txtLayoutEmail.getEditText().getText().toString(),
                            txtLayoutPassword.getEditText().getText().toString(),
                            txtLayoutFirstName.getEditText().getText().toString(),
                            txtLayoutLastName.getEditText().getText().toString(),
                            role);

                }
            }
        });
    }

    public void onRadioButtonClick() {
        radioGroupSelectRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonParticipant:
                        role = Role.PARTICIPANT;
                        break;
                    case R.id.radioButtonResearcher:
                        role = Role.RESEARCHER;
                        break;
                }
            }
        });
    }

    private void userRegister(String email, String password, String firstName, String lastName, Role role) {

        auth.createUserWithEmailAndPassword(email.trim(), password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Register successfully");
                    UserDocument userDocument = new UserDocument();
                    userDocument.onCreteUser(firstName, lastName, role);

                    if (role.equals(getString(R.string.researcher))) {
                        Intent intent = new Intent(RegisterActivity.this, ResearcherHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(RegisterActivity.this, ParticipantHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                } else {
                    Log.d(TAG, "Register unsuccessful");
                }
            }
        });
    }


}