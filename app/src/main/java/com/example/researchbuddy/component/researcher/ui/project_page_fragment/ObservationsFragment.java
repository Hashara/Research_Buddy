package com.example.researchbuddy.component.researcher.ui.project_page_fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.appsearch.AppSearchResult.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.researchbuddy.component.researcher.AudioCaptureActivity;
import com.example.researchbuddy.component.researcher.ProjectPageActivity;
import com.example.researchbuddy.databinding.FragmentObservationsBinding;
import com.example.researchbuddy.model.PageViewModel;
import com.example.researchbuddy.model.ProjectModel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class ObservationsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentObservationsBinding binding;
    private Context mContext;
    private ProjectModel project;

    private String TAG = "ObservationsFragment";
    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;
    private static int IMAGE_CAPTURE_CODE = 102;
    private static int MICROPHONE_RECORD_CODE = 103;
    private Uri videoPath;
    private Uri imagePath;


    public static ObservationsFragment newInstance(Context mContext, int index, ProjectModel project) {
        ObservationsFragment fragment = new ObservationsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.mContext = mContext;
        fragment.project = project;
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentObservationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button btn_add_video = binding.btnAddVideo;
        final Button btn_add_image = binding.btnAddImage;
        final Button btn_add_audio = binding.btnAddAudio;

        // get camera permission
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Log.d(TAG, "Camera is detected");
            getCameraPermission();
        } else {
            Log.d(TAG, "No camera is detected");
        }

        btn_add_video.setOnClickListener(view -> {
            recordVideoButtonPressed(view);
        });

        btn_add_image.setOnClickListener(view -> {
            captureImageOnButtonPressed(view);
        });

        btn_add_audio.setOnClickListener(view -> {
            recordAudioOnButtonPressed();
        });

        return root;
    }

    // video
    private void recordVideoButtonPressed(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        videoPath = Uri.fromFile(mContext.getExternalFilesDir("Projects/" + project.getProjectName() + "/videos/" + getFileName() + ".mp4" ));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoPath);
        // todo: change video path
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }

    private String getFileName(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        Date dt = new Date();
        return sdf.format(dt);
    }

    //image
    private void captureImageOnButtonPressed(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        videoPath = Uri.fromFile(mContext.getExternalFilesDir("Projects/" + project.getProjectName() + "/videos/" + getFileName() + ".mp4" ));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoPath);
        // todo: change image saving path
        startActivityForResult(intent, IMAGE_CAPTURE_CODE);
    }

    //audio
    private void recordAudioOnButtonPressed(){
        Intent intent = new Intent(mContext, AudioCaptureActivity.class);
        intent.putExtra("project", project);
        mContext.startActivity(intent);
    }

    private void getCameraPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_RECORD_CODE){
            if (resultCode == RESULT_OK){

                videoPath = data.getData();
                Log.d(TAG, "video is recorded and available at path" + videoPath);

            }
            else if(resultCode == RESULT_CANCELED){
                Log.d(TAG, "Recording video is cancelled");
            } else{
                Log.d(TAG, "Recording video has got some error");
            }
        }
        else if (requestCode == CAMERA_PERMISSION_CODE){
            if (resultCode == RESULT_OK){

                imagePath = data.getData();
                Log.d(TAG, "image is saved at" + imagePath);

            }
            else if(resultCode == RESULT_CANCELED){
                Log.d(TAG, "Capturing image is cancelled");
            } else{
                Log.d(TAG, "Capturing image has got some error");
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}