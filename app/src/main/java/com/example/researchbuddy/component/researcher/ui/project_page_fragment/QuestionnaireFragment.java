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
import com.example.researchbuddy.component.researcher.FormPageActivity;
import com.example.researchbuddy.databinding.FragmentQuestionnariesBinding;
import com.example.researchbuddy.model.PageViewModel;
import com.example.researchbuddy.model.ProjectModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuestionnaireFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentQuestionnariesBinding binding;
    private Context context;
    private ProjectModel project;

    // todo: bind with project
    public static QuestionnaireFragment newInstance(Context context, int index, ProjectModel project) {
        QuestionnaireFragment fragment = new QuestionnaireFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
//        fragment.setArguments(bundle);
        fragment.context = context;
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

        binding = FragmentQuestionnariesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // initialize views
        final Button btn_create_form = binding.btnCreateForm;
        final Button btn_view_draft_forms = binding.btnViewDraftForms;
        final Button btn_view_published_forms = binding.btnViewPublishedForms;

/*        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        // todo: add on click listeners
        btn_create_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FormCreateActivity.class);
                intent.putExtra("project", project);
                context.startActivity(intent);
            }
        });

        btn_view_draft_forms.setOnClickListener(view -> {
            Intent intent = new Intent(context, FormPageActivity.class);
            intent.putExtra("project", project);
            context.startActivity(intent);
        });

        btn_view_published_forms.setOnClickListener(view -> {
            Intent intent = new Intent(context, FormPageActivity.class);
            intent.putExtra("project", project);
            context.startActivity(intent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}