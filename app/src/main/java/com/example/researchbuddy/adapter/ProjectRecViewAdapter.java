package com.example.researchbuddy.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchbuddy.R;
import com.example.researchbuddy.component.researcher.ProjectPageActivity;
import com.example.researchbuddy.model.ProjectModel;

import java.util.ArrayList;

public class ProjectRecViewAdapter extends RecyclerView.Adapter<ProjectRecViewAdapter.ViewHolder> {
    private static final String TAG = "ProjectRecViewAdapter";

    private ArrayList<ProjectModel> projects = new ArrayList<>();

    private Context context;

    public ProjectRecViewAdapter(Context context) {
        this.context = context;
    }

    public void setProjects(ArrayList<ProjectModel> projects) {
        this.projects = projects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtFolderName.setText(projects.get(position).getProjectName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: get project details and passsed to project page
                Log.d(TAG, "Clicked on " + projects.get(position).getProjectName());
//                Intent intent = new Intent(context, ProjectPageActivity.class);
                Intent intent = new Intent(context, ProjectPageActivity.class);
                context.startActivity(intent);
//                Toast.makeText(context, projects.get(position).getProjectName() + " selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView parent;
        private TextView txtFolderName;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            imageView = itemView.findViewById(R.id.image);
            txtFolderName = itemView.findViewById(R.id.txtFolderName);
        }
    }
}
