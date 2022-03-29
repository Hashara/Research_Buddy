package com.example.researchbuddy.component.participant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.researchbuddy.R;
import com.example.researchbuddy.component.researcher.FormPageActivity;
import com.example.researchbuddy.model.type.FormStatusType;

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


        Intent intent = new Intent(ParticipantHomeActivity.this, FormPageActivity.class);
        intent.putExtra("formStatusType", FormStatusType.FILLING);
        startActivity(intent);
    }
}