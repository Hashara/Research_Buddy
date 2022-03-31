package com.example.researchbuddy.component.researcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.researchbuddy.R;
import com.example.researchbuddy.adapter.ProjectRecViewAdapter;
import com.example.researchbuddy.db.UserDocument;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.service.FileService;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ResearcherHomeActivity extends AppCompatActivity {
    private static final String TAG = "ResearcherHomeActivity";

    private RecyclerView projectRecView;

    private ProgressBar progressBar;
    private View dialogView;
    private ArrayList<ProjectModel> projects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_researcher_home);

        //create project folder if not exits
        FileService.writeFolder(this, this,"Projects");

        //action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initView();

        getProjects();
    }

    private void initView() {

        projectRecView = findViewById(R.id.projectRecView);
        progressBar = findViewById(R.id.progress_bar_loading);

    }

    public void setProjects(ArrayList<ProjectModel> projects) {
        this.projects = projects;
    }


    // get project from db
    public void getProjects() {
        Log.d(TAG, "Project fetching");

        Context context = this;
        Activity activity = this;

        projectRecView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        CollectionReference projectRef = FirebaseFirestore.getInstance().collection("projects");
        projectRef
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            if (document.exists()) {
                                ProjectModel project = document.toObject(ProjectModel.class);
                                Log.d(TAG, document.getId());
                                project.setProjectId(document.getId());
                                projects.add(project);
                                Log.d(TAG, project.toString());
                                FileService.writeFolder(context, activity,"Projects/" + project.getProjectName());
                            }

                        }
                        ProjectRecViewAdapter adapter = new ProjectRecViewAdapter(context);
                        adapter.setProjects(projects);

                        projectRecView.setAdapter(adapter);
                        projectRecView.setLayoutManager(new GridLayoutManager(context, 2));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                    }
                });

        projectRecView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
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
        CreateProjectActivity createProjectActivity = new CreateProjectActivity();
        createProjectActivity.initViews(dialogView);
        createProjectActivity.validateInput();
        createProjectActivity.setCheckboxListeners();

        MaterialAlertDialogBuilder materialAlertDialog =
                new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setTitle("Create new project")
                .setMessage("Project Template")
                .setPositiveButton("create", null)
                .setNegativeButton("cancel", null);

        AlertDialog projectDialog = materialAlertDialog.show();
        projectDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(createProjectActivity.getAwesomeValidation().validate()){
                    if(createProjectActivity.validateInputModes()){
                        createProjectActivity.createProject(dialogView);
                        projectDialog.dismiss();
                        // todo: change to add only the new project
                        startActivity(getIntent());
                        ;
                    }
                    else {
                        Toast.makeText(dialogView.getContext(), "Select at least one mode", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}