package com.example.researchbuddy.model;

import java.io.Serializable;

public class FormResponseModel implements Serializable {

    private FormModel form;
    private String participantId;

    public FormResponseModel() {
    }

    public FormModel getForm() {
        return form;
    }

    public void setForm(FormModel form) {
        this.form = form;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    @Override
    public String toString() {
        return "FormResponseModel{" +
                "form=" + form +
                ", participantId='" + participantId + '\'' +
                '}';
    }
}
