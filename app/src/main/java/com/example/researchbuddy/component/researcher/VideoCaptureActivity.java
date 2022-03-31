package com.example.researchbuddy.component.researcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.researchbuddy.R;
import com.example.researchbuddy.model.ProjectModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoCaptureActivity extends AppCompatActivity {
    private String TAG = "VideoCaptureActivity";


    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;
    private Uri videoPath;

    private ProjectModel project;
    private Button btn_record;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_capture);
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Log.d(TAG, "Camera is detected");

            // get variables
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                Log.d(TAG, "Bundle not null.....");
                project = (ProjectModel) getIntent().getSerializableExtra("project");

                getCameraPermission();
                recordVideoButtonPressed();
            }
        } else {
            Log.d(TAG, "No camera is detected");
            Toast.makeText(this, "No camera detected", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public void recordVideoButtonPressed() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }

    private void getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_RECORD_CODE) {
            if (resultCode == RESULT_OK) {
                videoPath = data.getData();
                saveFile(getRealPathFromURI(videoPath));
                Log.d(TAG, "video is recorded and available at path" + videoPath);

            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Recording video is cancelled");
            } else {
                Log.d(TAG, "Recording video has got some error");
            }
        }
    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private String getFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        Date dt = new Date();
        return sdf.format(dt);
    }

    private void saveFile(String sourceFilename) {
        String destinationFilename = this.getExternalFilesDir("Projects/" + project.getProjectName() + "/Videos/").getAbsolutePath()
                + File.separatorChar + getFileName() + ".mp4";

        Log.d(TAG, destinationFilename);
        Log.d(TAG, sourceFilename);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}