package com.example.researchbuddy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchbuddy.R;
import com.example.researchbuddy.component.researcher.ProjectPageActivity;
import com.example.researchbuddy.component.researcher.ResearcherHomeActivity;
import com.example.researchbuddy.db.ProjectDocument;
import com.example.researchbuddy.model.ProjectModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
        notifyDataSetChanged();
    }

    public void addProject(ProjectModel project) {
        projects.add(project);
        notifyItemInserted(projects.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtFolderName.setText(projects.get(position).getProjectName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked on " + projects.get(position).getProjectName());
                Intent intent = new Intent(context, ProjectPageActivity.class);
                intent.putExtra("project", projects.get(position));
                context.startActivity(intent);
            }
        });

        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialog =
                        new MaterialAlertDialogBuilder(context)
                                .setTitle("Do you want to delete this project?")
                                .setMessage("You will no longer able to add items to this project")
                                .setPositiveButton("Yes", null)
                                .setNegativeButton("No", null);

                AlertDialog deletetDialog = materialAlertDialog.show();
                deletetDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ProjectDocument projectDocument = new ProjectDocument();
                                projectDocument.deleteProject(projects.get(position));
//                                todo : call removeItem method within firebase Onsuccess
                                removeItem(position);
                                deletetDialog.dismiss();
//                                Toast.makeText(context, "Project deleted successfully",
//                                        Toast.LENGTH_SHORT).show();

                            }
                        });

                return true;
            }
        });

    }

    public void removeItem(int position) {
        projects.remove(position);
        notifyItemRemoved(position);
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
