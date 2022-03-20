package com.example.researchbuddy.component.researcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.researchbuddy.R;
import com.example.researchbuddy.adapter.ProjectRecViewAdapter;
import com.example.researchbuddy.db.ProjectDocument;
import com.example.researchbuddy.db.UserDocument;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.CollectionTypes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.checkbox.MaterialCheckBox;
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

                        // todo: change to add only the new project
                        startActivity(getIntent());;
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