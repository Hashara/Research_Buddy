package com.example.researchbuddy.service;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.researchbuddy.MainActivity;
import com.example.researchbuddy.R;
import com.example.researchbuddy.db.FormDocument;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileService {

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


    public void exportEmailInCSV(Context context, String folderPath,String formId ) throws IOException {
        {

            File folder = new File(context.getExternalFilesDir(folderPath).getAbsolutePath());
//            Log.d(TAG, context.getExternalFilesDir("").getAbsolutePath()
//                    + folderPath);

            boolean var = false;
            if (!folder.exists())
                var = folder.mkdir();

            System.out.println("" + var);


            final String filename = folder.toString() + "/" + formId + ".csv";

            // show waiting screen
            CharSequence contentTitle = context.getString(R.string.app_name);
            final ProgressDialog progDailog = ProgressDialog.show(
                    context, contentTitle, "please wait",
                    true);//please wait
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {




                }
            };

            new Thread() {
                public void run() {
                    try {

                        FileWriter fw = new FileWriter(filename);

                        // get data from db and write
                        FormDocument formDocument = new FormDocument();
                        formDocument.getResponse(formId, fw, context, filename);

                    } catch (Exception e) {
                    }
                    handler.sendEmptyMessage(0);
                    progDailog.dismiss();
                }
            }.start();

        }

    }
}
