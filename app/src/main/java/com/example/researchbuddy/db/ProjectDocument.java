package com.example.researchbuddy.db;

import android.app.ProgressDialog;
import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.researchbuddy.adapter.ProjectRecViewAdapter;
import com.example.researchbuddy.component.researcher.ProjectPageActivity;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.CollectionTypes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "ProjectDocument";
    private static final String COLLECTION = "projects";

    String userId = firebaseUser.getUid();

    public void onCreateProject(ProjectModel projectModel) {
        projectModel.setUserId(userId);

        Task<DocumentReference> task = db.collection(COLLECTION)
                .add(projectModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Project Details added successfully with Id: "
                                + documentReference.getId());
                        setProjectIdField(documentReference.getId());
                        Log.d(TAG, "successfully updated Project ID with Id:"
                                + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Project Details : ", e);
                    }
                });

    }

    public void deleteProject(ProjectModel projectModel, ProjectRecViewAdapter projectRecViewAdapter,
                              int position, ProgressDialog progressDialog) {
        db.collection(COLLECTION).document(projectModel.getProjectId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, projectModel.getProjectName() +
                                " project DocumentSnapshot successfully deleted!");
                        progressDialog.dismiss();
                        projectRecViewAdapter.removeItem(position);
                        ImageDocument imageDocument = new ImageDocument();
                        imageDocument.deleteImages(projectModel);
                        VideoDocument videoDocument = new VideoDocument();
                        videoDocument.deleteVideos(projectModel);
                        FormDocument formDocument = new FormDocument();
                        formDocument.deleteForms(projectModel);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, projectModel.getProjectName() +
                                " Error deleting document", e);
                    }
                });

    }

    public void setProjectIdField(String docId) {
        db.collection(COLLECTION).document(docId).update(
                "projectId", docId
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "project id updated successfully!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating project id", e);
            }
        });
    }

    public void addCollectionTypes(ProjectModel projectModel, List<CollectionTypes> collectionTypes,
    ProjectPageActivity projectPageActivity) {
        Log.d(TAG, collectionTypes.toString());
        db.collection(COLLECTION).document(projectModel.getProjectId()).update(
                "collectionTypes", collectionTypes
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, projectModel.getProjectName() +
                        " project collectionTypes updated successfully!");
                projectModel.setCollectionTypes(collectionTypes);
                projectPageActivity.reloadIntent(projectModel);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, projectModel.getProjectName() +
                        " Error updating collectionTypes", e);
            }
        });
    }

}
