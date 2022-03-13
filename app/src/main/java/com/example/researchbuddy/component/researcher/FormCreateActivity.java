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
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.researchbuddy.R;
import com.example.researchbuddy.model.FormModel;
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

    private ArrayList<FormModel> formItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_create);

        initViews();

        // todo: get data from ui

//        loadFormItems();


        btn_add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFormItem();
            }
        });
    }

    private void initViews() {
        edt_from_description = findViewById(R.id.edt_from_description);
        edt_txt_form_title = findViewById(R.id.edt_txt_form_title);
        btn_add_question = findViewById(R.id.btn_add_question);
        btn_submit = findViewById(R.id.btn_submit);

        scroll = findViewById(R.id.scroll);
        linear_layout_parent = findViewById(R.id.linear_layout_parent);
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

        linear_layout_parent.addView(rowView, linear_layout_parent.getChildCount());
    }


    public void onDeleteFormItem(View view) {
        Log.d(TAG, "On delete view");
        linear_layout_parent.removeView((View) view.getParent());
    }


}