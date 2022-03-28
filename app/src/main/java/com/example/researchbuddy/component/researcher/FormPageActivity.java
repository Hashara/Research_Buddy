package com.example.researchbuddy.component.researcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.researchbuddy.R;
import com.example.researchbuddy.adapter.FormRecViewAdapter;
import com.example.researchbuddy.databinding.ActivityFormCreateBinding;
import com.example.researchbuddy.databinding.ActivityFormPageBinding;
import com.example.researchbuddy.db.UserDocument;
import com.example.researchbuddy.model.FormModel;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.FormStatusType;
import com.example.researchbuddy.service.FIleWriter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FormPageActivity extends AppCompatActivity {


    private static final String TAG = "FormPageActivity";

    private RecyclerView formRecView;

    private ProgressBar progressBar;
    private View dialogView;
    private ArrayList<FormModel> forms = new ArrayList<>();

    private ProjectModel project;
    private FormStatusType formStatusType;

    private ActivityFormPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_page);

        //action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding = ActivityFormPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            project = (ProjectModel) getIntent().getSerializableExtra("project");
            formStatusType = (FormStatusType) getIntent().getSerializableExtra("formStatusType");

            Log.d(TAG, project.toString());
            initViews();
            getForms();

        }

    }


    private void initViews() {

        formRecView = findViewById(R.id.formtRecView);
        progressBar = findViewById(R.id.progress_bar_loading);

    }

    public void setFormModels(ArrayList<FormModel> formModels) {
        this.forms = formModels;
    }


    // get project from db
    public void getForms() {
        Log.d(TAG, "Form fetching");

        Context context = this;
        Activity activity = this;

        formRecView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        CollectionReference projectRef = FirebaseFirestore.getInstance().collection("forms");

        switch (formStatusType) {
            case DRAFT:
                projectRef
                        .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .whereEqualTo("published", false)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot document : documents) {
                                    if (document.exists()) {
                                        FormModel form = document.toObject(FormModel.class);
                                        Log.d(TAG, document.getId());
                                        form.setProjectId(document.getId());
                                        forms.add(form);
                                        Log.d(TAG, form.toString());
                                    }

                                }
                                FormRecViewAdapter adapter = new FormRecViewAdapter(context, project, FormStatusType.DRAFT);
                                adapter.setForms(forms);

                                formRecView.setAdapter(adapter);
                                formRecView.setLayoutManager(new GridLayoutManager(context, 2));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, e.toString());
                            }
                        });
                break;
            case PUBLISHED:
                projectRef
                        .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .whereEqualTo("published", true)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot document : documents) {
                                    if (document.exists()) {
                                        FormModel form = document.toObject(FormModel.class);
                                        Log.d(TAG, document.getId());
                                        form.setProjectId(document.getId());
                                        forms.add(form);
                                        Log.d(TAG, form.toString());
                                    }

                                }
                                FormRecViewAdapter adapter = new FormRecViewAdapter(context, project, FormStatusType.PUBLISHED);
                                adapter.setForms(forms);

                                formRecView.setAdapter(adapter);
                                formRecView.setLayoutManager(new GridLayoutManager(context, 2));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, e.toString());
                            }
                        });
                break;
            case FILLING:
                projectRef
                        .whereEqualTo("published", true)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot document : documents) {
                                    if (document.exists()) {
                                        FormModel form = document.toObject(FormModel.class);
                                        Log.d(TAG, document.getId());
                                        form.setProjectId(document.getId());
                                        forms.add(form);
                                        Log.d(TAG, form.toString());
                                    }

                                }
                                FormRecViewAdapter adapter = new FormRecViewAdapter(context, project, FormStatusType.FILLING);
                                adapter.setForms(forms);

                                formRecView.setAdapter(adapter);
                                formRecView.setLayoutManager(new GridLayoutManager(context, 2));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, e.toString());
                            }
                        });
                break;
            default:
                break;

        }


        formRecView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    // action bar
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