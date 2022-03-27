package com.example.researchbuddy.component.researcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.researchbuddy.R;
import com.example.researchbuddy.db.FormDocument;
import com.example.researchbuddy.model.FormItemModel;
import com.example.researchbuddy.model.FormModel;
import com.example.researchbuddy.model.ProjectModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private ProjectModel project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_display);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            form = (FormModel) getIntent().getSerializableExtra("form");
            project = (ProjectModel) getIntent().getSerializableExtra("project");
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

        createFormItemUI();

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
                    answer_text.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                    answer_layout.addView((View) answer_text);
                    break;

                case MULTIPLE_CHOICE:
                    RadioGroup answer_radio_group = new RadioGroup(this);

                    for (String choice : formItem.getAnswerList()) {
                        RadioButton radio_btn = new RadioButton(this);
                        radio_btn.setText(choice);
//                        radio_btn.setId(i + 100);
                        answer_radio_group.addView(radio_btn);
                    }

                    // todo: add this when form filling
/*                    answer_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            RadioButton radioButton = (RadioButton) findViewById(i);

                            formItem.setTextAnswer(radioButton.getText().toString());
                        }
                    });*/
                    answer_layout.addView((View) answer_radio_group);
                    break;

                case CHECK_BOXES:
                    for (String choice : formItem.getAnswerList()) {
                        CheckBox check_box = new CheckBox(this);
                        check_box.setText(choice);

                        // todo: add onclick listener when form filling
                        answer_layout.addView((View) check_box);

                    }
                    break;
                default:
                    break;
            }

            linear_layout_parent.addView(rowView, linear_layout_parent.getChildCount());
        }

    }
}