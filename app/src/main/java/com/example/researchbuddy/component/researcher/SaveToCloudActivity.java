package com.example.researchbuddy.component.researcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.researchbuddy.R;
import com.example.researchbuddy.adapter.ProjectRecViewAdapter;
import com.example.researchbuddy.db.ImageDocument;
import com.example.researchbuddy.db.ProjectDocument;
import com.example.researchbuddy.db.VideoDocument;
import com.example.researchbuddy.model.ImageModel;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.VideoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SaveToCloudActivity extends AppCompatActivity {

    private static final String TAG = "SaveToCloudActivity";
    private Uri imageUri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private ProjectModel project;
    private Button btnUploadImage;
    private Button btnUploadVideo;
    private ListView imagesListView;
    private ListView videosListView;
    private boolean isVideo = false;
    private List<ImageModel> imageListItems = new ArrayList<>();
    private List<String> imageNamesListItems = new ArrayList<>();
    private List<VideoModel> videoListItems = new ArrayList<>();
    private List<String> videoNamesListItems = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userId = firebaseUser.getUid();
    private ProgressDialog deleteProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_to_cloud);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(TAG, "bundle != null");
            project = (ProjectModel) getIntent().getSerializableExtra("project");
            Log.d(TAG, project.toString());
        }

        btnUploadImage = findViewById(R.id.uploadimagebtn);
        btnUploadVideo = findViewById(R.id.uploadvideobtn);
        imagesListView = findViewById(R.id.cloud_images_list_view);
        videosListView = findViewById(R.id.cloud_videos_list_view);

        setCloudImagesList();
        setCloudVideosList();

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

    public void setCloudImagesList() {

        CollectionReference imagesRef = db.collection("images");
        imagesRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("projectId", project.getProjectId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            if (document.exists()) {
                                ImageModel image = document.toObject(ImageModel.class);
                                Log.d(TAG, document.getId());
                                imageListItems.add(image);
                                imageNamesListItems.add(image.getImageName());
                            }

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>
                                (SaveToCloudActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        imageNamesListItems);
                        imagesListView.setAdapter(adapter);
                        imagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                ImageModel imageModel = imageListItems.get(position);
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageModel.getUrl()));
                                startActivity(browserIntent);
                            }
                        });
                        imagesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                                ImageModel imageModel = imageListItems.get(position);
                                MaterialAlertDialogBuilder materialAlertDialog =
                                        new MaterialAlertDialogBuilder(SaveToCloudActivity.this)
                                                .setTitle("Do you want to delete image " +
                                                        imageModel.getImageName() +"?")
                                                .setMessage("Image will be permanently deleted from cloud storage?")
                                                .setPositiveButton("Yes", null)
                                                .setNegativeButton("No", null);

                                AlertDialog deletetDialog = materialAlertDialog.show();
                                deletetDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                deletetDialog.dismiss();
                                                deleteProgressDialog = new ProgressDialog(SaveToCloudActivity.this);
                                                deleteProgressDialog.setTitle("Deleting image....");
                                                deleteProgressDialog.show();
                                                ImageDocument imageDocument = new ImageDocument();
                                                imageDocument.deleteSingleImage(imageModel,
                                                        deleteProgressDialog, SaveToCloudActivity.this);
                                            }
                                        });

                                return true;
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(imageNamesListItems.isEmpty()){
                            List<String> emptyList = Arrays.asList("No images uploaded");
                            ArrayAdapter<String> adapter = new ArrayAdapter<>
                                    (SaveToCloudActivity.this,
                                            android.R.layout.simple_list_item_1,
                                            emptyList);
                            imagesListView.setAdapter(adapter);
                        }
                    }
                });


    }

    public void setCloudVideosList() {
        CollectionReference videosRef = db.collection("videos");
        videosRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("projectId", project.getProjectId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            if (document.exists()) {
                                VideoModel video = document.toObject(VideoModel.class);
                                Log.d(TAG, document.getId());
                                videoListItems.add(video);
                                videoNamesListItems.add(video.getVideoName());
                            }

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>
                                (SaveToCloudActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        videoNamesListItems);
                        videosListView.setAdapter(adapter);
                        videosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                VideoModel videoModel = videoListItems.get(position);
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoModel.getUrl()));
                                startActivity(browserIntent);
                            }
                        });
                        videosListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                                VideoModel videoModel = videoListItems.get(position);
                                MaterialAlertDialogBuilder materialAlertDialog =
                                        new MaterialAlertDialogBuilder(SaveToCloudActivity.this)
                                                .setTitle("Do you want to delete video " +
                                                        videoModel.getVideoName() +"?")
                                                .setMessage("Video will be permanently deleted from cloud storage?")
                                                .setPositiveButton("Yes", null)
                                                .setNegativeButton("No", null);

                                AlertDialog deletetDialog = materialAlertDialog.show();
                                deletetDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                deletetDialog.dismiss();
                                                deleteProgressDialog = new ProgressDialog(SaveToCloudActivity.this);
                                                deleteProgressDialog.setTitle("Deleting video....");
                                                deleteProgressDialog.show();
                                                VideoDocument videoDocument = new VideoDocument();
                                                videoDocument.deleteSingleVideo(videoModel,
                                                        deleteProgressDialog, SaveToCloudActivity.this);
                                            }
                                        });

                                return true;
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(videoNamesListItems.isEmpty()){
                            List<String> emptyList = Arrays.asList("No videos uploaded");
                            ArrayAdapter<String> adapter = new ArrayAdapter<>
                                    (SaveToCloudActivity.this,
                                            android.R.layout.simple_list_item_1,
                                            emptyList);
                            videosListView.setAdapter(adapter);
                        }
                    }
                });;
    }

    public void reloadIntent() {
        Log.d(TAG, "media uploaded to cloud");
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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
                                videoModel.setVideoName(fileName + ".mp4");
                                videoModel.setUrl(uri.toString());
                                VideoDocument videoDocument = new VideoDocument();
                                videoDocument.onCreateProject(videoModel, SaveToCloudActivity.this);
                            } else {
                                ImageModel imageModel = new ImageModel();
                                imageModel.setProjectId(project.getProjectId());
                                imageModel.setImageName(fileName + ".jpeg");
                                imageModel.setUrl(uri.toString());
                                ImageDocument imageDocument = new ImageDocument();
                                imageDocument.onCreateProject(imageModel, SaveToCloudActivity.this);
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