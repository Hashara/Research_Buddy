package com.example.researchbuddy.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.researchbuddy.model.ImageModel;
import com.example.researchbuddy.model.ProjectModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ImageDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "ImageDocument";
    private static final String COLLECTION = "images";

    String userId = firebaseUser.getUid();

    public void onCreateProject(ImageModel imageModel) {
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

}
