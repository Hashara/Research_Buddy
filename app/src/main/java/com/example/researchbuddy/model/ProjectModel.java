package com.example.researchbuddy.model;

import com.example.researchbuddy.model.type.CollectionTypes;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class ProjectModel implements Serializable {
    private String projectName;
    private String projectId;
    private String userId;

    private List<CollectionTypes> collectionTypes;

//    public ProjectModel(String projectName, String projectId, List<CollectionTypes> collectionTypes) {
//        this.projectName = projectName;
//        this.projectId = projectId;
//        this.collectionTypes = collectionTypes;
//    }


    public ProjectModel() {
    }

    public ProjectModel(String projectName, List<CollectionTypes> collectionTypes) {
        this.projectName = projectName;
        this.collectionTypes = collectionTypes;
    }

    // todo: remove this
    public ProjectModel(String projectName) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ProjectModel{" +
                "projectName='" + projectName + '\'' +
                ", projectId='" + projectId + '\'' +
                ", userId='" + userId + '\'' +
                ", collectionTypes=" + collectionTypes +
                '}';
    }
}
