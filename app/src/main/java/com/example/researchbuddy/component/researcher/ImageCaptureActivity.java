package com.example.researchbuddy.component.researcher;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.example.researchbuddy.R;
import com.example.researchbuddy.model.ProjectModel;
import com.google.common.collect.BiMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ImageCaptureActivity extends AppCompatActivity {

    private String TAG = "ImageCaptureActivity";


    private ProjectModel project;

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String currentPhotoPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iamge_capture);
        this.imageView = (ImageView) this.findViewById(R.id.imageView1);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(TAG, "Bundle not null.....");
            project = (ProjectModel) getIntent().getSerializableExtra("project");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {


            }

            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            try {
                File imageFile = File.createTempFile(getFileName(), "jpg", storageDirectory);

                currentPhotoPath = imageFile.getAbsolutePath();

                Uri uri = FileProvider.getUriForFile(ImageCaptureActivity.this,
                        "com.example.researchbuddy.fileprovider", imageFile);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            } catch (Exception e) {

            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = BitmapFactory.decodeFile(currentPhotoPath);
//            imageView.setImageBitmap((Bitmap) data.getExtras().get("data"));

            String imageFileName = getFileName() + ".png";

            String path = this.getExternalFilesDir("Projects/" + project.getProjectName() + "/Images/").getAbsolutePath() + "/" + imageFileName;

            FileOutputStream fos = null;
            BufferedOutputStream bos = null;

            try {
                fos = new FileOutputStream(path);
                bos = new BufferedOutputStream(fos);
                photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.flush();
                        //bos.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (bos != null) {
                    try {
                        bos.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }


                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                bos = null;
                fos = null;
                finish();
            }

        }
    }

    private String getFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        Date dt = new Date();
        return sdf.format(dt);
    }

}