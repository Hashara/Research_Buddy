package com.example.researchbuddy.component.researcher;

import android.view.View;
import android.widget.CompoundButton;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.researchbuddy.R;
import com.example.researchbuddy.db.ProjectDocument;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.CollectionTypes;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class CreateProjectActivity{
    private static final String TAG = "CreateProjectActivity";

    private TextInputLayout projectName;
    private MaterialCheckBox questionnaire, observation, interview;
    private String projectNameValue;
    private boolean questionnaireVal, observationVal, interviewVal;
    private AwesomeValidation awesomeValidation;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_project);
//    }

    public void initViews(View view) {
        projectName = view.findViewById(R.id.txtLayoutProjectName);
        questionnaire = view.findViewById(R.id.checkQuestionnaire);
        observation = view.findViewById(R.id.checkObservation);
        interview = view.findViewById(R.id.checkInterview);
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

    @Override
    public String toString() {
        return "CreateProjectActivity{" +
                "projectNameValue='" + projectNameValue + '\'' +
                ", questionnaireVal=" + questionnaireVal +
                ", observationVal=" + observationVal +
                ", interviewVal=" + interviewVal +
                '}';
    }

    public void createProject(View view) {
        projectNameValue = projectName.getEditText().getText().toString();
        System.out.println(this);
        List<CollectionTypes> selectedModes = new ArrayList<>();
        if(questionnaireVal) selectedModes.add(CollectionTypes.FORMS);
        if(observationVal) selectedModes.add(CollectionTypes.OBSERVATION);
        if(interviewVal) selectedModes.add(CollectionTypes.CALL_INTERVIEW);

        ProjectModel projectModel = new ProjectModel(projectNameValue, selectedModes);
        ProjectDocument projectDocument = new ProjectDocument();
        projectDocument.onCreateProject(projectModel);
    }

    public AwesomeValidation getAwesomeValidation() {
        return awesomeValidation;
    }

    public void validateInput() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(projectName.getEditText(), RegexTemplate.NOT_EMPTY,
                "Project name is empty");
    }

    public boolean validateInputData(){
        projectNameValue = projectName.getEditText().getText().toString();
        System.out.println(this);
        if(projectNameValue.isEmpty()) return false;
        return questionnaireVal || observationVal || interviewVal;
    }
}