package com.example.researchbuddy.component.researcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.researchbuddy.R;
import com.example.researchbuddy.db.UserDocument;

public class ProjectPageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnQuestionnaires;
    private Button btnObservation;
    private Button btnCallInterviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_page);

        //action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // add back button
        // https://stackoverflow.com/questions/15686555/display-back-button-on-action-barhttps://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
        initView();

    }

    private void initView() {
        btnCallInterviews = findViewById(R.id.btn_call_interview);
        btnQuestionnaires = findViewById(R.id.btn_questionnaire);
        btnObservation = findViewById(R.id.btn_observation);

        btnObservation.setOnClickListener(this);
        btnCallInterviews.setOnClickListener(this);
        btnQuestionnaires.setOnClickListener(this);
        // todo: check project data collection type and set visibility
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_only_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                UserDocument userDocument = new UserDocument();
                userDocument.logout(this);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View view) {
        // todo: redirect to the page
        switch (view.getId()) {
            case R.id.btn_questionnaire:
                Toast.makeText(this, "Questionnaire", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_call_interview:
                Toast.makeText(this, "Call interview", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_observation:
                Toast.makeText(this, "Observation", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}