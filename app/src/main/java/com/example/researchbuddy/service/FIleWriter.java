package com.example.researchbuddy.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.researchbuddy.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FIleWriter {

    private static String TAG = "FileWriter";
    private static final int FILE_WRITE_PERMISSION_REQUEST_CODE = 7;


    public static void writeFolder(Context context, Activity activity, String folder_path) {
        Log.d(TAG, folder_path);
        getWritePermission(activity);
//
        File mydir = new File(context.getExternalFilesDir(folder_path).getAbsolutePath());
        Log.d(TAG, context.getExternalFilesDir(folder_path).getAbsolutePath());
        if (!mydir.exists()) {
            mydir.mkdirs();
            Log.d(TAG, "file created");

        } else {
            Log.d(TAG, "file exists");

        }



    }

    private static void getWritePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]
                        {
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }


}
