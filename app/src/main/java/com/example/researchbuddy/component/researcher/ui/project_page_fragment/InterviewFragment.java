package com.example.researchbuddy.component.researcher.ui.project_page_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.researchbuddy.component.researcher.FormCreateActivity;
import com.example.researchbuddy.component.researcher.InterviewCreateActivity;
import com.example.researchbuddy.databinding.FragmentInterviewsBinding;
import com.example.researchbuddy.databinding.FragmentQuestionnariesBinding;
import com.example.researchbuddy.model.PageViewModel;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.FormStatusType;

/**
 * A placeholder fragment containing a simple view.
 */
public class InterviewFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentInterviewsBinding binding;
    private Context context;
    private ProjectModel project;

    public static InterviewFragment newInstance(Context context, int index, ProjectModel projectModel) {
        InterviewFragment fragment = new InterviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.context = context;
        fragment.project = projectModel;
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

        binding = FragmentInterviewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // initialize views
        final Button btn_create_recording = binding.btnCreateRecording;
//        final Button btn_view_recordings = binding.btnViewRecordings;
/*
        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        // on click listeners
        btn_create_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InterviewCreateActivity.class);
                intent.putExtra("project", project);
                context.startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}