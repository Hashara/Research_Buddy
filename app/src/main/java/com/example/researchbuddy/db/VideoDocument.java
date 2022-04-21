package com.example.researchbuddy.db;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.researchbuddy.component.researcher.SaveToCloudActivity;
import com.example.researchbuddy.model.ImageModel;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.VideoModel;
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

public class VideoDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "VideoDocument";
    private static final String COLLECTION = "videos";

    String userId = firebaseUser.getUid();

    public void onCreateProject(VideoModel videoModel, SaveToCloudActivity saveToCloudActivity) {
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

    public void deleteVideos(ProjectModel projectModel) {
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
                        deleteStorageVideo(url);
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, projectModel.getProjectName() +
                                " Error deleting video document", e);
                    }
                });
    }

    public void deleteStorageVideo(String url) {
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d(TAG, "onSuccess: deleted video from storage");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "error deleting video from storage");
            }
        });
    }

    public void deleteSingleVideo(VideoModel videoModel, ProgressDialog progressDialog,
                                  SaveToCloudActivity saveToCloudActivity) {
        db.collection(COLLECTION).document(videoModel.getVideoId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Successfully deleted single video");
                        deleteStorageVideo(videoModel.getUrl());
                        progressDialog.dismiss();
                        saveToCloudActivity.reloadIntent();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error deleting single video");
                    }
                });
    }

}
