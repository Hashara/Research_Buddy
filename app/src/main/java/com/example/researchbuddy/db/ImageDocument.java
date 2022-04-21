package com.example.researchbuddy.db;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.researchbuddy.component.researcher.SaveToCloudActivity;
import com.example.researchbuddy.model.ImageModel;
import com.example.researchbuddy.model.ProjectModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ImageDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "ImageDocument";
    private static final String COLLECTION = "images";

    String userId = firebaseUser.getUid();

    public void onCreateProject(ImageModel imageModel, SaveToCloudActivity saveToCloudActivity) {
        imageModel.setUserId(userId);

        Task<DocumentReference> task = db.collection(COLLECTION)
                .add(imageModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Image added successfully with Id: "
                                + documentReference.getId());
                        setImageIdField(documentReference.getId());
                        Log.d(TAG, "successfully updated Image ID with Id:"
                                + documentReference.getId());
                        saveToCloudActivity.reloadIntent();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Project Details : ", e);
                    }
                });

    }

    public void setImageIdField(String docId) {
        db.collection(COLLECTION).document(docId).update(
                "imageId", docId
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "image id updated successfully!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating image id", e);
            }
        });
    }

    public void deleteImages(ProjectModel projectModel) {

        Query images_query = db.collection(COLLECTION)
                .whereEqualTo("projectId", projectModel.getProjectId());
        images_query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    if (document.exists()) {
                        String url = document.getString("url");
                        document.getReference().delete();
                        deleteStorageImage(url);
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, projectModel.getProjectName() +
                                " Error deleting image document", e);
                    }
                });
    }

    public void deleteStorageImage(String url) {
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d(TAG, "onSuccess: deleted image from storage");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "error deleting image from storage");
            }
        });
    }

    public void deleteSingleImage(ImageModel imageModel, ProgressDialog progressDialog,
                                  SaveToCloudActivity saveToCloudActivity) {
        db.collection(COLLECTION).document(imageModel.getImageId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Successfully deleted single image");
                        deleteStorageImage(imageModel.getUrl());
                        progressDialog.dismiss();
                        saveToCloudActivity.reloadIntent();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error deleting single image");
                    }
                });
    }

}
