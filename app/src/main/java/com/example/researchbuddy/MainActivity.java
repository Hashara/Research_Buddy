package com.example.researchbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.researchbuddy.component.auth.LoginActivity;
import com.example.researchbuddy.db.FirestoreSetting;
import com.example.researchbuddy.db.UserDocument;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 4000;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        ImageView image = findViewById(R.id.imageView);
        Animation animSideSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_slide);
        image.startAnimation(animSideSlide);
        new FirestoreSetting().setup();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (user != null) {
                    UserDocument userDocument = new UserDocument();
                    userDocument.checkUserRoleAndRedirect(MainActivity.this);
                } else {
                    Log.d(TAG, "Directing to login screen");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        }, SPLASH_SCREEN_TIME_OUT);

    }

}