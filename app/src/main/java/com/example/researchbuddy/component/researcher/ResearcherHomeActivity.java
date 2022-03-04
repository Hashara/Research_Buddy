package com.example.researchbuddy.component.researcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.researchbuddy.R;
import com.example.researchbuddy.adapter.ProjectRecViewAdapter;
import com.example.researchbuddy.model.ProjectModel;

import java.util.ArrayList;

public class ResearcherHomeActivity extends AppCompatActivity {
    private static final String TAG = "ResearcherHomeActivity";

    private RecyclerView projectRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_researcher_home);

        //action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initView();

        addProjects();
    }

    private void initView() {
        projectRecView = findViewById(R.id.projectRecView);
    }

    private void addProjects() {
        Log.d(TAG, "creating project views");
        ArrayList<ProjectModel> projects = new ArrayList<>();

        // todo: get project from database
        projects.add(new ProjectModel("Demo"));
        projects.add(new ProjectModel("Demo1"));
        projects.add(new ProjectModel("Demo2"));
        projects.add(new ProjectModel("Demo3"));
        projects.add(new ProjectModel("Demo4"));

        ProjectRecViewAdapter adapter = new ProjectRecViewAdapter(this);
        adapter.setProjects(projects);

        projectRecView.setAdapter(adapter);
        projectRecView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    // action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.researcher_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add_project:
                // todo: create add project method
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}