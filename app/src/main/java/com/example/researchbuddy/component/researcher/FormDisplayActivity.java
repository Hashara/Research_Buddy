package com.example.researchbuddy.component.researcher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.researchbuddy.R;
import com.example.researchbuddy.model.FormModel;

public class FormDisplayActivity extends AppCompatActivity {

    private FormModel form;
    private String TAG = "FormDisplayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_display);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            form = (FormModel) getIntent().getSerializableExtra("form");
        Log.d(TAG, form.toString());
    }
}