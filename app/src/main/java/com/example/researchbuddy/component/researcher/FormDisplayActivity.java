package com.example.researchbuddy.component.researcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.researchbuddy.R;
import com.example.researchbuddy.db.FormDocument;
import com.example.researchbuddy.model.FormItemModel;
import com.example.researchbuddy.model.FormModel;
import com.example.researchbuddy.model.FormResponseModel;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.FormStatusType;
import com.example.researchbuddy.service.FileService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FormDisplayActivity extends AppCompatActivity {

    private FormModel form;
    private String TAG = "FormDisplayActivity";

    private TextView txt_form_title;
    private TextView txt_form_description;

    private LinearLayout linear_layout_parent;

    private FloatingActionButton btn_edit;
    private FloatingActionButton btn_save;
    private FloatingActionButton btn_publish;
    private FloatingActionButton btn_home;
    private FloatingActionButton btn_download;
    private FloatingActionButton btn_back;

    private ProjectModel project;
    private FormStatusType formStatusType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_display);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            form = (FormModel) getIntent().getSerializableExtra("form");
            formStatusType = (FormStatusType) getIntent().getSerializableExtra("formStatusType");

            if (formStatusType.equals(FormStatusType.FILLING)) {

            } else {
                project = (ProjectModel) getIntent().getSerializableExtra("project");
            }

            initViews();
        }
        Log.d(TAG, form.toString());

    }

    private void initViews() {
        txt_form_title = findViewById(R.id.txt_form_title);
        txt_form_description = findViewById(R.id.txt_from_description);
        linear_layout_parent = findViewById(R.id.linear_layout_parent);

        txt_form_title.setText(form.getTitle());
        txt_form_description.setText(form.getDescription());

        btn_publish = findViewById(R.id.btn_publish);
        btn_edit = findViewById(R.id.btn_add_question);
        btn_save = findViewById(R.id.btn_save_draft);
        btn_home = findViewById(R.id.btn_home);
        btn_back = findViewById(R.id.btn_back_btn);
        btn_download = findViewById(R.id.btn_download_responses);

        createFormItemUI();

        switch (formStatusType) {
            case BUILDING:
                btn_back.setVisibility(View.GONE);
                btn_home.setVisibility(View.GONE);
                btn_download.setVisibility(View.GONE);
                // on click listeners
                btn_edit.setOnClickListener(view -> {
                    // back
                    finish();
                });

                btn_publish.setOnClickListener(view -> {
                    form.setPublished(true);
                    FormDocument formDocument = new FormDocument();
                    formDocument.onCreateForm(form, this, true, btn_publish, btn_save, btn_home);
                });

                btn_save.setOnClickListener(view -> {
                    form.setPublished(false);
                    FormDocument formDocument = new FormDocument();
                    formDocument.onCreateForm(form, this, false, btn_publish, btn_save, btn_home);
                });

                btn_home.setOnClickListener(view -> {
                    Intent intent = new Intent(FormDisplayActivity.this, ProjectPageActivity.class);
                    intent.putExtra("project", project);
                    startActivity(intent);
                });
                break;
            case DRAFT:
                btn_back.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_download.setVisibility(View.GONE);
                btn_home.setVisibility(View.VISIBLE);

                // on click listeners
                btn_edit.setOnClickListener(view -> {

                    //  redirect to edit page
                    Log.d(TAG, "onclick listener btn edit");
                    Intent intent = new Intent(this, FormCreateActivity.class);
                    intent.putExtra("project", project);
                    intent.putExtra("formStatusType", FormStatusType.DRAFT);
                    intent.putExtra("form", form);
                    startActivity(intent);
                });

                btn_publish.setOnClickListener(view -> {
                    form.setPublished(true);
                    form.setProjectId(project.getProjectId());
//                    Log.d(TAG, " form id...." + form.getFormId() + " project id ...." + form.getProjectId());
                    FormDocument formDocument = new FormDocument();
                    formDocument.onCreateForm(form, this, true, btn_publish, btn_save, btn_home);
                });


                btn_home.setOnClickListener(view -> {
                    finish();
                });
                break;
            case PUBLISHED:
                btn_edit.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_home.setVisibility(View.VISIBLE);
                btn_publish.setVisibility(View.GONE);
                btn_download.setVisibility(View.VISIBLE);
                // on click listeners
                btn_back.setOnClickListener(view -> {
                    // back
                    finish();
                });

                btn_download.setOnClickListener(view -> {
                    FileService fileService = new FileService();
                    try {
                        fileService.exportEmailInCSV(this,
                                "/Projects/" + project.getProjectName() + "/Forms", form.getFormId());

                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                });

                btn_home.setOnClickListener(view -> {
                    finish();
                });
                break;
            case FILLING:
                btn_edit.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_home.setVisibility(View.VISIBLE);
                btn_publish.setVisibility(View.VISIBLE);
                btn_download.setVisibility(View.GONE);
                // on click listeners
                btn_back.setOnClickListener(view -> {
                    // back
                    finish();
                });

                btn_publish.setOnClickListener(view -> {
                    Log.d(TAG, "sending form responses");
                    FormResponseModel response = new FormResponseModel();
                    response.setForm(form);
                    response.setFormId(form.getFormId());
                    response.setParticipantId(FirebaseAuth.getInstance().getUid());

                    FormDocument formDocument = new FormDocument();
                    formDocument.onResponseForm(response, this, this, btn_publish);
                });

                btn_home.setOnClickListener(view -> {
                    finish();
                });
                break;
            default:
                break;

        }


    }

    private void createFormItemUI() {

        for (FormItemModel formItem :
                form.getItems()) {


            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.form_answering_item, null);

            // initialize view items
            TextView question = rowView.findViewById(R.id.txt_question);
            LinearLayout answer_layout = rowView.findViewById(R.id.linear_layout_answers);

            question.setText(formItem.getQuestion());

            switch (formItem.getType()) {
                case TEXT:
                    EditText answer_text = new EditText(this);
                    answer_text.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    answer_layout.addView((View) answer_text);

                    if (formStatusType.equals(FormStatusType.FILLING)) {
                        answer_text.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if (charSequence.length() != 0) {
                                    Log.d(TAG, charSequence.toString());
                                    formItem.setTextAnswer(charSequence.toString());
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                            }
                        });
                    }
                    break;

                case MULTIPLE_CHOICE:
                    RadioGroup answer_radio_group = new RadioGroup(this);

                    for (String choice : formItem.getAnswerList()) {
                        RadioButton radio_btn = new RadioButton(this);
                        radio_btn.setText(choice);
//                        radio_btn.setId(i + 100);
                        answer_radio_group.addView(radio_btn);
                    }

                    if (formStatusType.equals(FormStatusType.FILLING)) {

                        answer_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                RadioButton radioButton = (RadioButton) findViewById(i);

                                formItem.setTextAnswer(radioButton.getText().toString());
                            }
                        });
                    }
                    answer_layout.addView((View) answer_radio_group);
                    break;

                case CHECK_BOXES:
                    for (String choice : formItem.getAnswerList()) {
                        CheckBox check_box = new CheckBox(this);
                        check_box.setText(choice);

                        answer_layout.addView((View) check_box);

                        if (formStatusType.equals(FormStatusType.FILLING)) {
                            check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b) {
                                        // add to array
                                        if (formItem.getTextAnswer() == null){
                                            formItem.setTextAnswer(check_box.getText().toString());
                                        }
                                        else {
                                            formItem.setTextAnswer(formItem.getTextAnswer() + ";" + check_box.getText().toString());
                                        }

                                    } else {
                                        // remove from array
                                        ArrayList<String> userAnswers = new ArrayList<String>(Arrays.asList(formItem.getTextAnswer().split("\\s*;\\s*")));
                                        userAnswers.remove(check_box.getText().toString());

                                        String s = "";
                                        Log.d(TAG + "user answers" ,userAnswers.toString());

                                        for (String answer :
                                                userAnswers) {
                                            if (!answer.equals("null")){
                                                s = s + answer + ";";
                                            }
                                        }

                                        formItem.setTextAnswer(s);
                                    }
                                }
                            });
                        }
                    }
                    break;
                default:
                    break;
            }

            linear_layout_parent.addView(rowView, linear_layout_parent.getChildCount());
        }

    }
}