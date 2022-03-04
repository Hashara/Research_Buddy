package com.example.researchbuddy.model;

import com.example.researchbuddy.model.type.CollectionTypes;

import java.util.List;

public class Project {
    private String projectName;
    private String projectId;
    private List<CollectionTypes> collectionTypes;

    public Project(String projectName, String projectId, List<CollectionTypes> collectionTypes) {
        this.projectName = projectName;
        this.projectId = projectId;
        this.collectionTypes = collectionTypes;
    }

    public Project(String projectName, List<CollectionTypes> collectionTypes) {
        this.projectName = projectName;
        this.collectionTypes = collectionTypes;
    }

    // todo: remove this
    public Project(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public List<CollectionTypes> getCollectionTypes() {
        return collectionTypes;
    }

    public void setCollectionTypes(List<CollectionTypes> collectionTypes) {
        this.collectionTypes = collectionTypes;
    }
}
