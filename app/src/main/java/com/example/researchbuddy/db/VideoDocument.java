package com.example.researchbuddy.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.researchbuddy.model.VideoModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class VideoDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "VideoDocument";
    private static final String COLLECTION = "videos";

    String userId = firebaseUser.getUid();

    public void onCreateProject(VideoModel videoModel) {
        videoModel.setUserId(userId);

        Task<DocumentReference> task = db.collection(COLLECTION)
                .add(videoModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Video added successfully with Id: "
                                + documentReference.getId());
                        setVideoIdField(documentReference.getId());
                        Log.d(TAG, "successfully updated Video ID with Id:"
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

    public void setVideoIdField(String docId) {
        db.collection(COLLECTION).document(docId).update(
                "videoId", docId
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "video id updated successfully!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating video id", e);
            }
        });
    }
}
