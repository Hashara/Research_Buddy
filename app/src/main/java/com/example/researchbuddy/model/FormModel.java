package com.example.researchbuddy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class FormModel implements Serializable {
    private String title;
    private String description;
    private ArrayList<FormItemModel> items;
    private boolean isPublished;
    private String projectId;
    private String userId;
    private String formId;

    public FormModel(String title, String description, ArrayList<FormItemModel> items) {
        this.title = title;
        this.description = description;
        this.items = items;
        formId = UUID.randomUUID().toString();
    }

    public FormModel() {
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<FormItemModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<FormItemModel> items) {
        this.items = items;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "FormModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", items=" + items +
                ", isPublished=" + isPublished +
                '}';
    }
}
