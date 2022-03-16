package com.example.researchbuddy.component.researcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.researchbuddy.R;
import com.example.researchbuddy.adapter.ProjectRecViewAdapter;
import com.example.researchbuddy.db.ProjectDocument;
import com.example.researchbuddy.db.UserDocument;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.CollectionTypes;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ResearcherHomeActivity extends AppCompatActivity {
    private static final String TAG = "ResearcherHomeActivity";

    private RecyclerView projectRecView;

    private View dialogView;

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

                // Inflate Custom alert dialog view
                dialogView = LayoutInflater.from(this)
                        .inflate(R.layout.activity_create_project, null, false);
                launchDialogView();
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

    public void launchDialogView() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder =
                new MaterialAlertDialogBuilder(this);

        CreateProjectActivity createProjectActivity = new CreateProjectActivity();
        createProjectActivity.initViews(dialogView);
        createProjectActivity.setCheckboxListeners();

        materialAlertDialogBuilder.setView(dialogView)
                .setTitle("Create new project")
                .setMessage("Project Template")
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ResearcherHomeActivity.this, "Create is clicked", Toast.LENGTH_SHORT).show();
                        createProjectActivity.createProject(dialogView);
                    }
                })
                .setNegativeButton("cancle", null)
                .show();
    }

//    public void createNewProject() {
//
//        String[] multiItems = { "Questionnaires", "Observations", "Interviews"};
//        boolean[] checkedItems = {false, false, false};
//        List<CollectionTypes> selectedModes = new ArrayList<>();
//
//        new MaterialAlertDialogBuilder(this)
//                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(ResearcherHomeActivity.this, "Create is clicked", Toast.LENGTH_SHORT).show();
////                        ProjectDocument projectDocument = new ProjectDocument();
////                        projectDocument.onCreateProject();
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .setMultiChoiceItems(multiItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i, boolean checked) {
//                        System.out.println(multiItems[i] + " : " + checked);
//                        if(checked){
//                            switch (multiItems[i]){
//                                case "Questionnaires":
//                                    selectedModes.add(CollectionTypes.FORMS);
//                                    break;
//                                case "Observations":
//                                    selectedModes.add(CollectionTypes.OBSERVATION);
//                                    break;
//                                case "Interviews":
//                                    selectedModes.add(CollectionTypes.CALL_INTERVIEW);
//                                    break;
//                            }
//                        }
//                    }
//                })
//                .setTitle("Project Template")
//                .show();
//    }
}