package com.example.researchbuddy.component.researcher;

import static com.example.researchbuddy.model.type.CollectionTypes.CALL_INTERVIEW;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.researchbuddy.R;
import com.example.researchbuddy.db.ProjectDocument;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.CollectionTypes;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class UpdateProjectActivity {

    private static final String TAG = "UpdateProjectActivity";
    private ProjectModel project;
    private MaterialCheckBox questionnaire, observation, interview;
    private boolean questionnaireVal, observationVal, interviewVal;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_mode_form);
//    }

    public UpdateProjectActivity(ProjectModel projectModel) {
        this.project = projectModel;
    }

    public void initViews(View view) {
        questionnaire = view.findViewById(R.id.checkQuestionnaire);
        observation = view.findViewById(R.id.checkObservation);
        interview = view.findViewById(R.id.checkInterview);

        List<CollectionTypes> initialModes = project.getCollectionTypes();
        Log.d(TAG, initialModes.toString());
        for (CollectionTypes mode : initialModes) {
            switch (mode) {
                case CALL_INTERVIEW:
                    interview.setChecked(true);
                    interviewVal = true;
                    break;
                case FORMS:
                    questionnaire.setChecked(true);
                    questionnaireVal = true;
                    break;
                case OBSERVATION:
                    observation.setChecked(true);
                    observationVal = true;
                    break;

            }

        }
    }

    public void setCheckboxListeners() {
        questionnaire.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        questionnaireVal = b;
                    }
                }
        );
        observation.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        observationVal = b;
                    }
                }
        );
        interview.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        interviewVal = b;
                    }
                }
        );
    }

    public boolean validateInputModes() {
        return questionnaireVal || observationVal || interviewVal;
    }


    public void updateModes(ProjectModel projectModel, ProjectPageActivity projectPageActivity) {
        List<CollectionTypes> selectedModes = new ArrayList<>();
        if (questionnaireVal) selectedModes.add(CollectionTypes.FORMS);
        if (observationVal) selectedModes.add(CollectionTypes.OBSERVATION);
        if (interviewVal) selectedModes.add(CALL_INTERVIEW);
        Log.d(TAG, selectedModes.toString());

        ProjectDocument projectDocument = new ProjectDocument();
        projectDocument.addCollectionTypes(projectModel, selectedModes, projectPageActivity);
    }
}