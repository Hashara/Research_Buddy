package com.example.researchbuddy.db;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.researchbuddy.adapter.FormRecViewAdapter;
import com.example.researchbuddy.model.FormItemModel;
import com.example.researchbuddy.model.FormModel;
import com.example.researchbuddy.model.FormResponseModel;
import com.example.researchbuddy.model.type.FormStatusType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FormDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "FormDocument";

    public void onCreateForm(FormModel formModel, Context context, boolean isPublishing, FloatingActionButton btn_publish,
                             FloatingActionButton btn_save, FloatingActionButton btn_home) {
        Log.d(TAG, formModel.getFormId());
        db.collection("forms")
                .document(formModel.getFormId())
                .set(formModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (isPublishing) {
                            Log.d(TAG, "Form published successfully");
                            Toast.makeText(context, "Form published successfully", Toast.LENGTH_SHORT).show();
                            btn_publish.setVisibility(View.GONE);
                            btn_save.setVisibility(View.GONE);
                            btn_home.setVisibility(View.VISIBLE);
                        } else {
                            Log.d(TAG, "Form added successfully");
                            Toast.makeText(context, "Form added successfully", Toast.LENGTH_SHORT).show();
                            btn_save.setVisibility(View.GONE);
                            btn_home.setVisibility(View.VISIBLE);

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

    public void onResponseForm(FormResponseModel formResponseModel, Context context, Activity activity, FloatingActionButton btn_publish) {
        Log.d(TAG, formResponseModel.getParticipantId() + formResponseModel.getForm().getFormId());
        Log.d(TAG, formResponseModel.toString());
        db.collection("responses")
                .document(formResponseModel.getParticipantId() + formResponseModel.getForm().getFormId())
                .set(formResponseModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Response added successfully", Toast.LENGTH_SHORT).show();
                        btn_publish.setVisibility(View.GONE);
                        activity.finish();
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

    public void getResponse(String formId, FileWriter fw, Context context, String filename) throws IOException {
        // write column headers
        db.collection("forms")
                .whereEqualTo("formId", formId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            if (document.exists()) {
                                FormModel form = document.toObject(FormModel.class);
                                Log.d(TAG, document.getId());


                                try {
                                    Log.d(TAG, "start write in csv....");
                                    for (FormItemModel formItem :
                                            form.getItems()) {
                                        fw.append(formItem.getQuestion());
                                        fw.append(',');
                                    }
                                    fw.append('\n');

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }

                        }

                        writeResponsesInCsv(formId, fw, context, filename);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                    }
                });


        // write responses


    }

    private void writeResponsesInCsv(String formId, FileWriter fw, Context context, String filename) {
        db.collection("responses")
                .whereEqualTo("formId", formId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            if (document.exists()) {
                                FormResponseModel response = document.toObject(FormResponseModel.class);
                                Log.d(TAG, document.getId());


                                try {
                                    Log.d(TAG, "start write in csv....");
                                    for (FormItemModel formItem :
                                            response.getForm().getItems()) {
                                        fw.append(formItem.getTextAnswer());
                                        fw.append(',');
                                    }
                                    fw.append('\n');

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }

                        }
                        try {
                            fw.flush();
                            fw.close();
                            Toast.makeText(context, "View responses at " + filename, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                    }
                });
    }
}
