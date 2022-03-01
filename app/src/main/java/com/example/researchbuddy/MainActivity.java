package com.example.researchbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ImageView image=findViewById(R.id.imageView);
        Animation animSideSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.side_slide);
        image.startAnimation(animSideSlide);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              /*  if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    boolean accepted = DatabaseHelper.getInstance(MainActivity.this).isAllowed();

                    if(accepted){
                        Intent intent = new Intent(MainActivity.this, permission_list.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(MainActivity.this, appPermission.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }



                } else {
                    Intent i = new Intent(MainActivity.this,
                            Home.class);
                    //Intent is used to switch from one activity to another.

                    startActivity(i);
                    //invoke the SecondActivity.
                }
                finish();*/
                //the current activity will get finished.

                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                Log.d("Info","hello");
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }

}