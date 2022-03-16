package com.example.researchbuddy.component.researcher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.researchbuddy.R;
import com.example.researchbuddy.model.FormModel;

public class FormDisplayActivity extends AppCompatActivity {

    private FormModel form;
    private String TAG = "FormDisplayActivity";

    private TextView txt_form_title;
    private TextView txt_form_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_display);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            form = (FormModel) getIntent().getSerializableExtra("form");
            initViews();
        }
        Log.d(TAG, form.toString());

    }

    private void initViews() {
        txt_form_title = findViewById(R.id.txt_form_title);
        txt_form_description = findViewById(R.id.txt_from_description);

        txt_form_title.setText(form.getTitle());
        txt_form_description.setText(form.getDescription());

    }
}