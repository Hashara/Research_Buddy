package com.example.researchbuddy.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FormModel implements Serializable {
    private String title;
    private String description;
    private ArrayList<FormItemModel> items;

    public FormModel(String title, String description, ArrayList<FormItemModel> items) {
        this.title = title;
        this.description = description;
        this.items = items;
    }

    public FormModel() {
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

    @Override
    public String toString() {
        return "FormModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", items=" + items +
                '}';
    }
}
