package com.example.researchbuddy.service;

import android.os.Environment;

import java.io.File;

public class FIleWriter {

    public static void writeFolder(String folder_path){
        File f = new File(Environment.getExternalStorageDirectory(), folder_path);
        if (!f.exists()) {
            f.mkdirs();
        }
    }
}
