package com.example.researchbuddy.model;

import com.example.researchbuddy.model.type.FormItemType;

import java.util.ArrayList;
import java.util.Arrays;

public class FormModel {

    private String question;
    private int position;
    private FormItemType type;
    private ArrayList<String> answerList;
    private String textAnswer;

    public FormModel(String question, int position, FormItemType type, ArrayList<String> answerList, String textAnswer) {
        this.question = question;
        this.position = position;
        this.type = type;
        this.answerList = answerList;
        this.textAnswer = textAnswer;
    }

    public FormModel() {
    }

    public FormModel(String question) {
        this.question = question;

    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setType(FormItemType type) {
        this.type = type;
        if (type.equals(FormItemType.TEXT)) {
            answerList = null;
        } else {
            textAnswer = "";
            answerList = new ArrayList<>();
        }
    }

    public void setAnswerList(ArrayList<String> answerList) {
        this.answerList = answerList;
    }

    public void setAnswerList(String answerList) {
        this.answerList =  new ArrayList<String>(Arrays.asList(answerList.split("\\s*;\\s*")));
    }


    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public int getPosition() {
        return position;
    }

    public FormItemType getType() {
        return type;
    }

    public ArrayList<String> getAnswerList() {
        if (type.equals(FormItemType.TEXT)) {
            answerList = null;
        }
        return answerList;
    }

    public String getTextAnswer() {
        if (!type.equals(FormItemType.TEXT)) {
            textAnswer = "";
        }
        return textAnswer;
    }

    @Override
    public String toString() {
        return "FormModel{" +
                "question='" + question + '\'' +
                ", position=" + position +
                ", type=" + type +
                ", answerList=" + answerList +
                ", textAnswer='" + textAnswer + '\'' +
                '}';
    }
}
