package com.example.researchbuddy.component.researcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.researchbuddy.R;
import com.example.researchbuddy.databinding.ActivityFormCreateBinding;
import com.example.researchbuddy.model.FormItemModel;
import com.example.researchbuddy.model.FormModel;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.FormItemType;
import com.example.researchbuddy.model.type.FormStatusType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FormCreateActivity extends AppCompatActivity {

    private static final String TAG = "FormCreateActivity";

    private EditText edt_txt_form_title;
    private EditText edt_from_description;
    private ScrollView scroll;
    private LinearLayout linear_layout_parent;

    private FloatingActionButton btn_add_question;
    private FloatingActionButton btn_submit;

    private ProgressBar progressBar;

    private CardView title_card;
    private ProjectModel project;

    private ActivityFormCreateBinding binding;
    private FormModel form;
    private FormStatusType formStatusType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_create);

        Log.d(TAG, "on create");
        binding = ActivityFormCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            project = (ProjectModel) getIntent().getSerializableExtra("project");
            formStatusType = (FormStatusType) getIntent().getSerializableExtra("formStatusType");

            if (formStatusType.equals(FormStatusType.DRAFT)) {
                Log.d(TAG, "Editing a draft form");
                // for edit existing form
                form = (FormModel) getIntent().getSerializableExtra("form");
                Log.d(TAG, form.toString());

                initViews();

                // set title and description
                edt_txt_form_title.setText(form.getTitle());
                edt_from_description.setText(form.getDescription());

                // form items

                for (FormItemModel formItem :
                        form.getItems()) {
                    addFormItem(formItem);
                }

            } else {
                initViews();
                addFormItem();

            }
            Log.d(TAG, project.toString());

        }

    }

    private void initViews() {
        edt_from_description = findViewById(R.id.edt_from_description);
        edt_txt_form_title = findViewById(R.id.edt_txt_form_title);
        btn_add_question = findViewById(R.id.btn_add_question);
        btn_submit = findViewById(R.id.btn_submit);

        scroll = findViewById(R.id.scroll);
        linear_layout_parent = findViewById(R.id.linear_layout_parent);

        progressBar = findViewById(R.id.progress_bar_loading);

        title_card = findViewById(R.id.title_card);

        btn_add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFormItem();
            }
        });

        btn_submit.setOnClickListener(view -> {
            onClickSubmitButton();
        });

    }


    public void addFormItem() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.form_item, null);
        // Add the new row before the add field button.
        ImageButton btn_delete = rowView.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(view -> {
            linear_layout_parent.removeView((View) view.getParent());
        });

        // radio button functions
        RadioGroup radioGroup = rowView.findViewById(R.id.radio_group_question_type);
        EditText editTextAnswer = rowView.findViewById(R.id.edt_text_answer);
        TextView txtInstruction = rowView.findViewById(R.id.txt_instruction);

        int checkedRadioId = radioGroup.getCheckedRadioButtonId();

        switch (checkedRadioId) {
            case R.id.radio_checkboxes:
            case R.id.radio_multiple_choice:
                editTextAnswer.setVisibility(View.VISIBLE);
                txtInstruction.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_text:
                editTextAnswer.setVisibility(View.GONE);
                txtInstruction.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {

                    case R.id.radio_checkboxes:
                    case R.id.radio_multiple_choice:
                        editTextAnswer.setVisibility(View.VISIBLE);
                        txtInstruction.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_text:
                        editTextAnswer.setVisibility(View.GONE);
                        txtInstruction.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });
        linear_layout_parent.addView(rowView, linear_layout_parent.getChildCount());
    }


    public void addFormItem(FormItemModel formItemModel) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.form_item, null);
        // Add the new row before the add field button.
        ImageButton btn_delete = rowView.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(view -> {
            linear_layout_parent.removeView((View) view.getParent());
        });

        // radio button functions
        RadioGroup radioGroup = rowView.findViewById(R.id.radio_group_question_type);
        EditText editTextAnswer = rowView.findViewById(R.id.edt_text_answer);
        EditText editTextQuestion = rowView.findViewById(R.id.edt_text_question);
        TextView txtInstruction = rowView.findViewById(R.id.txt_instruction);

        int checkedRadioId = radioGroup.getCheckedRadioButtonId();

        switch (checkedRadioId) {
            case R.id.radio_checkboxes:
            case R.id.radio_multiple_choice:
                editTextAnswer.setVisibility(View.VISIBLE);
                txtInstruction.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_text:
                editTextAnswer.setVisibility(View.GONE);
                txtInstruction.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {

                    case R.id.radio_checkboxes:
                    case R.id.radio_multiple_choice:
                        editTextAnswer.setVisibility(View.VISIBLE);
                        txtInstruction.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_text:
                        editTextAnswer.setVisibility(View.GONE);
                        txtInstruction.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });
        editTextQuestion.setText(formItemModel.getQuestion());
        switch (formItemModel.getType()) {
            case CHECK_BOXES:
                radioGroup.check(R.id.radio_checkboxes);
                editTextAnswer.setText(getStringAnswerList(formItemModel.getAnswerList()));
                break;
            case MULTIPLE_CHOICE:
                radioGroup.check(R.id.radio_multiple_choice);
                editTextAnswer.setText(getStringAnswerList(formItemModel.getAnswerList()));
                break;
            case TEXT:
                radioGroup.check(R.id.radio_text);
                break;
            default:
                radioGroup.check(R.id.radio_text);
                break;
        }
        linear_layout_parent.addView(rowView, linear_layout_parent.getChildCount());
    }


    private String getStringAnswerList(ArrayList<String> answerList) {
        String stringAnswerList = "";

        for (String answer :
                answerList) {
            stringAnswerList = stringAnswerList + answer + ";";
        }

        return stringAnswerList;
    }

    public void onClickSubmitButton() {
        progressBar.setVisibility(View.VISIBLE);
        linear_layout_parent.setVisibility(View.GONE);
        title_card.setVisibility(View.GONE);


        final int childCount = linear_layout_parent.getChildCount();
        ArrayList<FormItemModel> formItems = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            progressBar.setProgress(i / childCount);
            View v = linear_layout_parent.getChildAt(i);
            FormItemModel formItemModel = new FormItemModel();

            formItemModel.setPosition(i);


            EditText txt_answer_list = v.findViewById(R.id.edt_text_answer);
            EditText txt_question = v.findViewById(R.id.edt_text_question);
            RadioGroup radioGroup = v.findViewById(R.id.radio_group_question_type);
            int checkedRadioId = radioGroup.getCheckedRadioButtonId();

            switch (checkedRadioId) {
                case R.id.radio_checkboxes:
                    formItemModel.setType(FormItemType.CHECK_BOXES);
                    formItemModel.setAnswerListFromString(txt_answer_list.getText().toString());
                    break;
                case R.id.radio_multiple_choice:
                    formItemModel.setType(FormItemType.MULTIPLE_CHOICE);
                    formItemModel.setAnswerListFromString(txt_answer_list.getText().toString());
                    break;
                case R.id.radio_text:
                    formItemModel.setType(FormItemType.TEXT);
                    formItemModel.setAnswerList(new ArrayList<>());
                    break;
                default:
                    break;
            }
            formItemModel.setQuestion(txt_question.getText().toString());
            formItems.add(formItemModel);
        }

        Log.d(TAG, formItems.toString());

        String formTitle = edt_txt_form_title.getText().toString();
        String formDescription = edt_from_description.getText().toString();

        // create new form
        if (formStatusType == FormStatusType.BUILDING) {
            form = new FormModel(formTitle, formDescription, formItems);
            form.setProjectId(project.getProjectId());
            form.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        } else {
            form.setItems(formItems);
        }

        // pass object to next activity
        Intent intent = new Intent(this, FormDisplayActivity.class);
        intent.putExtra("form", form);
        intent.putExtra("project", project);
        intent.putExtra("formStatusType", FormStatusType.BUILDING);


        progressBar.setVisibility(View.GONE);
        linear_layout_parent.setVisibility(View.VISIBLE);
        title_card.setVisibility(View.VISIBLE);

        startActivity(intent);
    }

}