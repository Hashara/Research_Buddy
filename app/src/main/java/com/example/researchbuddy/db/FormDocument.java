package com.example.researchbuddy.db;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.researchbuddy.model.FormModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FormDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "FormDocument";

    public void onCreateForm(FormModel formModel, Context context, boolean isPublishing, FloatingActionButton btn_publish,
                             FloatingActionButton btn_save) {
        Log.d(TAG,formModel.getFormId());
        db.collection("forms")
                .document(formModel.getFormId())
                .set(formModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (isPublishing){
                            Log.d(TAG, "Form published successfully");
                            Toast.makeText(context, "Form published successfully", Toast.LENGTH_SHORT).show();
                            btn_publish.setVisibility(View.GONE);
                            btn_save.setVisibility(View.GONE);
                        }else {
                            Log.d(TAG, "Form added successfully");
                            Toast.makeText(context, "Form added successfully", Toast.LENGTH_SHORT).show();
                            btn_save.setVisibility(View.GONE);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Form : ", e);
                        Toast.makeText(context, "Error adding Form", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
