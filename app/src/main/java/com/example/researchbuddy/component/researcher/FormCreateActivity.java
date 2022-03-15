package com.example.researchbuddy.component.researcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.researchbuddy.MainActivity;
import com.example.researchbuddy.R;
import com.example.researchbuddy.model.FormModel;
import com.example.researchbuddy.model.type.FormItemType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FormCreateActivity extends AppCompatActivity {

    private static final String TAG = "FormCreateActivity";

    private EditText edt_txt_form_title;
    private EditText edt_from_description;
    private ScrollView scroll;
    private LinearLayout linear_layout_parent;

    private FloatingActionButton btn_add_question;
    private FloatingActionButton btn_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_create);

        initViews();

        // todo: get data from ui

//        loadFormItems();


    }

    private void initViews() {
        edt_from_description = findViewById(R.id.edt_from_description);
        edt_txt_form_title = findViewById(R.id.edt_txt_form_title);
        btn_add_question = findViewById(R.id.btn_add_question);
        btn_submit = findViewById(R.id.btn_submit);

        scroll = findViewById(R.id.scroll);
        linear_layout_parent = findViewById(R.id.linear_layout_parent);

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

//
//    private void loadFormItems() {
//        Log.d(TAG, "creating form views");
//
//
//        // todo: get from database if available
//        FormModel formItem1 = new FormModel("TEST");
//        formItem1.setPosition(formItems.size());
//        formItems.add(formItem1);
//
//    }

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

    public void onClickSubmitButton() {
        final int childCount = linear_layout_parent.getChildCount();
        ArrayList<FormModel> formItems = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View v = linear_layout_parent.getChildAt(i);
            FormModel formModel = new FormModel();

            formModel.setPosition(i);


            EditText txt_answer_list = v.findViewById(R.id.edt_text_answer);
            EditText txt_question = v.findViewById(R.id.edt_text_question);
            RadioGroup radioGroup = v.findViewById(R.id.radio_group_question_type);
            int checkedRadioId = radioGroup.getCheckedRadioButtonId();

            switch (checkedRadioId) {
                case R.id.radio_checkboxes:
                    formModel.setType(FormItemType.CHECK_BOXES);
                    formModel.setAnswerList(txt_answer_list.getText().toString());
                    break;
                case R.id.radio_multiple_choice:
                    formModel.setType(FormItemType.MULTIPLE_CHOICE);
                    formModel.setAnswerList(txt_answer_list.getText().toString());
                    break;
                case R.id.radio_text:
                    formModel.setType(FormItemType.TEXT);
                    formModel.setAnswerList(new ArrayList<>());
                    break;
                default:
                    break;
            }
            formModel.setQuestion(txt_question.getText().toString());
            formItems.add(formModel);
        }

        Log.d(TAG, formItems.toString());
    }

}