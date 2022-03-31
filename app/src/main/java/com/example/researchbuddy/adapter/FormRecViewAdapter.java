package com.example.researchbuddy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchbuddy.R;
import com.example.researchbuddy.component.researcher.FormDisplayActivity;
import com.example.researchbuddy.model.FormModel;
import com.example.researchbuddy.model.ProjectModel;
import com.example.researchbuddy.model.type.FormStatusType;

import java.util.ArrayList;

public class FormRecViewAdapter extends RecyclerView.Adapter<FormRecViewAdapter.ViewHolder> {
    private static final String TAG = "FormRecViewAdapter";

    private ArrayList<FormModel> forms = new ArrayList<>();

    private Context context;
    private ProjectModel project;
    private FormStatusType formStatusType;

    public FormRecViewAdapter(Context context, ProjectModel project, FormStatusType formStatusType) {
        this.context = context;
        this.project = project;
        this.formStatusType = formStatusType;
    }

    public void setForms(ArrayList<FormModel> forms) {
        this.forms = forms;
        notifyDataSetChanged();
    }

    public void addProject(FormModel form) {
        forms.add(form);
        notifyItemInserted(forms.size() - 1);
    }

    @NonNull
    @Override
    public FormRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_select_item, parent, false);
        FormRecViewAdapter.ViewHolder holder = new FormRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FormRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtFormTitle.setText(forms.get(position).getTitle());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked on " + forms.get(position).getTitle());

                Intent intent = new Intent(context, FormDisplayActivity.class);
                intent.putExtra("project", project);
                intent.putExtra("form", forms.get(position));
                intent.putExtra("formStatusType", formStatusType);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return forms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView parent;
        private TextView txtFormTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtFormTitle = itemView.findViewById(R.id.txt_form_title);
        }
    }
}
