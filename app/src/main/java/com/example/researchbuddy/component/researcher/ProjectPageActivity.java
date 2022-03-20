package com.example.researchbuddy.component.researcher;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.researchbuddy.R;
import com.example.researchbuddy.databinding.ActivityProjectPageBinding;
import com.example.researchbuddy.db.UserDocument;
import com.example.researchbuddy.model.FormModel;
import com.example.researchbuddy.model.ProjectModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.researchbuddy.adapter.SectionsPagerAdapter;

public class ProjectPageActivity extends AppCompatActivity {

    private ActivityProjectPageBinding binding;
    private ProjectModel project;
    private String TAG= "ProjectPageActivity";
//    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProjectPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            project = (ProjectModel) getIntent().getSerializableExtra("project");
            Log.d(TAG, project.toString());
            initViews();

            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), project);
            ViewPager viewPager = binding.viewPager;
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = binding.tabs;
            tabs.setupWithViewPager(viewPager);
        }




    }

    private void initViews() {

        // add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(project.getProjectName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_only_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                UserDocument userDocument = new UserDocument();
                userDocument.logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}