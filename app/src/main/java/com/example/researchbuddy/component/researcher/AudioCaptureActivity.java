package com.example.researchbuddy.component.researcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageView;
;
import com.example.researchbuddy.R;
import com.example.researchbuddy.adapter.SectionsPagerAdapter;
import com.example.researchbuddy.databinding.ActivityAudioCaptureBinding;
import com.example.researchbuddy.databinding.ActivityProjectPageBinding;
import com.example.researchbuddy.model.ProjectModel;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioCaptureActivity extends AppCompatActivity {

    private ImageView record_btn;
    private boolean isRecording = false;
    private int RECORD_PERMISSION_CODE = 104;
    private MediaRecorder mediaRecorder;
    private String recordFile;
    private ProjectModel project;

    private ActivityAudioCaptureBinding binding;
    private String TAG= "AudioCaptureActivity";

    private Chronometer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_capture);

        binding = ActivityAudioCaptureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            project = (ProjectModel) getIntent().getSerializableExtra("project");
            Log.d(TAG, project.toString());
            initViews();

        }
    }

    private void initViews() {
        record_btn = findViewById(R.id.record_btn);
        timer = findViewById(R.id.record_timer);


        record_btn.setOnClickListener(view1 -> {
            if (isRecording) {
                //stop recording
                stopRecording();
                record_btn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_circle_icons_mic_stopped, null));
                isRecording = !isRecording;

            } else {
                // start recording
                if (checkPermissions()) {
                    startRecording();
                    record_btn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_circle_icons_mic, null));
                    isRecording = !isRecording;

                }
            }
        });

    }

    private void stopRecording() {
        timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        String recordPath = this.getExternalFilesDir("/Projects/" + project.getProjectName() + "/Audios/").getAbsolutePath();
        recordFile = getFileName() + ".3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
        timer.start();
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_CODE);
            return false;
        }
    }


    private String getFileName(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        Date dt = new Date();
        return sdf.format(dt);
    }


}