package com.example.researchbuddy.component.participant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.researchbuddy.R;

public class ParticipantHomeActivity extends AppCompatActivity {
    private static final String TAG = "ResearcherHomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_home);

        //action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }
}