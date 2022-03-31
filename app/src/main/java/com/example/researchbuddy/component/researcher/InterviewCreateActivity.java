package com.example.researchbuddy.component.researcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.researchbuddy.R;
import com.example.researchbuddy.db.UserDocument;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;

public class InterviewCreateActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 10;
    private TextInputLayout mEditTextNumber;

    //Call Recording varibales
//    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
//    //    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
//    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp3";
//    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
//    private MediaRecorder recorder = null;
//    private int currentFormat = 0;
//    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP};
//    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};
//    AudioManager audioManager;
//    public static final int RECORD_AUDIO = 0;
//    public static final int WRITE_EXTERNAL_STORAGE = 1;
//    public static final int READ_EXTERNAL_STORAGE = 2;
//    private Button startRecBtn;
//    private Button stopRecBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_create);

        //action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mEditTextNumber = findViewById(R.id.edit_text_number);
        ImageView imageCall = findViewById(R.id.image_call);

        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

//        recording feature
//        startRecBtn = findViewById(R.id.btnStartRecord);
//        stopRecBtn = findViewById(R.id.btnStopRecord);
//
//        startRecBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startRecording();
//            }
//        });
//
//        stopRecBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stopRecording();
//            }
//        });
    }

    private void makePhoneCall() {
        String number = mEditTextNumber.getEditText().getText().toString();
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(InterviewCreateActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(InterviewCreateActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(InterviewCreateActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall();
                } else {
                    Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                }
//                recording options
//            case 0:
//                // Re-attempt file write
//                setReorder();
//            case 1:
//                prepareAndStart();
//            case 2:
//                startRec();
        }
    }

    // action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_only_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                UserDocument userDocument = new UserDocument();
                userDocument.logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    /////////////////////////////////////////////////////////////////////////////////////////////////////
//    recording feature methods
//public void startRecording(){
//    if (ActivityCompat.checkSelfPermission(InterviewCreateActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//
//        ActivityCompat.requestPermissions(InterviewCreateActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);
//
//    } else {
//        setReorder();
//    }
//}
//
//    public void setReorder(){
//        audioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
//        audioManager.setMode(AudioManager.MODE_IN_CALL);
//        audioManager.setSpeakerphoneOn(true);
//        recorder = new MediaRecorder();
////        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(output_formats[currentFormat]);
////        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        recorder.setOutputFile(getFilename());
//        recorder.setOnErrorListener(errorListener);
//        recorder.setOnInfoListener(infoListener);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            // Do the file write
//            prepareAndStart();
//        } else {
//            // Request permission from the user
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
//        }
//    }
//
//    public void prepareAndStart() {
//        if (ActivityCompat.checkSelfPermission(InterviewCreateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(InterviewCreateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
//
//        } else {
//            startRec();
//        }
//
//
//    }
//
//    public void startRec(){
//        try{
//            recorder.prepare();
//            recorder.start();
//
//        } catch (IllegalStateException e) {
//            Log.e("REDORDING :: ",e.getMessage());
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.e("REDORDING :: ",e.getMessage());
//            e.printStackTrace();
//        }
//    }


//    public void stopRecording(){
//        audioManager.setSpeakerphoneOn(false);
//
//        try{
//            if (null != recorder) {
//                recorder.stop();
//                Log.d("REDORDING STOP :: ", "recorder.stop();");
//                recorder.reset();
//                Log.d("REDORDING STOP :: ", "recorder.reset();");
//                recorder.release();
//                Log.d("REDORDING STOP :: ", "recorder.release();");
//
//                recorder = null;
//            }
//        }catch(RuntimeException stopException){
//            Log.e("REDORDING STOP :: ", "RuntimeException stopException");
//            Log.e("REDORDING STOP :: ",stopException.getMessage());
//            stopException.printStackTrace();
//        }
//    }
//
//    private String getFilename() {
////        String filepath = Environment.getExternalStorageDirectory().getPath();
//        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
//        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
//        Log.d("FILEPATH", filepath);
//
//        if (!file.exists()) {
//            file.mkdirs();
//            Log.d( "!file.exists","created file");
//
//        }
//        Log.d("BEFORE RETURN", "created file EXISTS");
//        Log.d("BEFORE RETURN", file.getAbsolutePath());
//        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
////        return (file.getAbsolutePath());
//
//    }
//
//    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
//        @Override
//        public void onError(MediaRecorder mr, int what, int extra) {
//            Toast.makeText(InterviewCreateActivity.this,
//                    "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
//        @Override
//        public void onInfo(MediaRecorder mr, int what, int extra) {
//            Toast.makeText(InterviewCreateActivity.this,
//                    "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT)
//                    .show();
//        }
//    };
}
