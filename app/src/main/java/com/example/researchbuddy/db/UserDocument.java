package com.example.researchbuddy.db;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.researchbuddy.component.auth.LoginActivity;
import com.example.researchbuddy.component.participant.ParticipantHomeActivity;
import com.example.researchbuddy.component.researcher.ResearcherHomeActivity;
import com.example.researchbuddy.model.UserModel;
import com.example.researchbuddy.model.type.Role;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class UserDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "FirebaseHelper";

    DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());

    public DocumentReference getDocRef() {
        return docRef;
    }

    // add  when creating a new user
    public void onCreteUser(String firstName, String lastName, Role role) {
        UserModel user = new UserModel(firstName, lastName, role);
        System.out.println(user.getRole());

        docRef
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Added user details successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Adding user details failed");
                    }
                });
    }

    // check user role and redirect
    public void checkUserRoleAndRedirect(Context context) {
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document.exists()) {
                    UserModel user = document.toObject(UserModel.class);
                    Log.d(TAG, user.toString());
                    Intent intent;
                    switch (user.getRole()) {
                        case RESEARCHER:
                            intent = new Intent(context, ResearcherHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            break;
                        case PARTICIPANT:
                            intent = new Intent(context, ParticipantHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            break;
                        default:
                            break;
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                    }
                });

    }


}
