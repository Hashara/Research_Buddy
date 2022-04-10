package com.example.researchbuddy.component.researcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.researchbuddy.R;
import com.example.researchbuddy.db.ImageDocument;
import com.example.researchbuddy.db.VideoDocument;
import com.example.researchbuddy.model.ImageModel;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.VideoModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveToCloudActivity extends AppCompatActivity {

    private static final String TAG = "SaveToCloudActivity";
    private Uri imageUri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private ProjectModel project;

    private RecyclerView imageRecView;
    private ProgressBar progressBar;

    private Button btnUploadImage;
    private Button btnUploadVideo;
    private boolean isVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_to_cloud);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            project = (ProjectModel) getIntent().getSerializableExtra("project");
            Log.d(TAG, project.toString());
        }

        btnUploadImage = findViewById(R.id.uploadimagebtn);
        btnUploadVideo = findViewById(R.id.uploadvideobtn);

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });
    }

    private void uploadContent() {
        if (imageUri != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading File....");
            progressDialog.show();


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String fileName = formatter.format(now);
            if (isVideo) {
                storageReference = FirebaseStorage.getInstance().getReference("videos/" + fileName);
            } else {
                storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);
            }

            UploadTask uploadTask = storageReference.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SaveToCloudActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, uri.toString());
                            if (isVideo) {
                                VideoModel videoModel = new VideoModel();
                                videoModel.setProjectId(project.getProjectId());
                                videoModel.setUrl(uri.toString());
                                VideoDocument videoDocument = new VideoDocument();
                                videoDocument.onCreateProject(videoModel);
                            } else {
                                ImageModel imageModel = new ImageModel();
                                imageModel.setProjectId(project.getProjectId());
                                imageModel.setUrl(uri.toString());
                                ImageDocument imageDocument = new ImageDocument();
                                imageDocument.onCreateProject(imageModel);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(SaveToCloudActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();


                }
            });

        }

    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
        isVideo = false;
    }

    private void selectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
        isVideo = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadContent();
        }
    }

}