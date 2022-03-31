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
import com.example.researchbuddy.component.researcher.FormDisplayActivity;
import com.example.researchbuddy.component.researcher.ImageCaptureActivity;
import com.example.researchbuddy.component.researcher.ProjectPageActivity;
import com.example.researchbuddy.component.researcher.VideoCaptureActivity;
import com.example.researchbuddy.databinding.FragmentObservationsBinding;
import com.example.researchbuddy.model.PageViewModel;
import com.example.researchbuddy.model.ProjectModel;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ObservationsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentObservationsBinding binding;
    private Context mContext;
    private ProjectModel project;

    private String TAG = "ObservationsFragment";
    private static int CAMERA_PERMISSION_CODE = 100;

    public static ObservationsFragment newInstance(Context mContext, int index, ProjectModel project) {
        ObservationsFragment fragment = new ObservationsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.mContext = mContext;
        fragment.project = project;
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
            recordVideoButtonPressed();

        });

        btn_add_image.setOnClickListener(view -> {
            captureImageOnButtonPressed();
        });

        btn_add_audio.setOnClickListener(view -> {
            recordAudioOnButtonPressed();
        });

        return root;
    }

    // video
    private void recordVideoButtonPressed() {
        Intent intent = new Intent(mContext, VideoCaptureActivity.class);
        intent.putExtra("project", project);
        mContext.startActivity(intent);
    }

    //image
    private void captureImageOnButtonPressed() {
        Intent intent = new Intent(mContext, ImageCaptureActivity.class);
        intent.putExtra("project", project);
        mContext.startActivity(intent);
    }

    //audio
    private void recordAudioOnButtonPressed() {
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}