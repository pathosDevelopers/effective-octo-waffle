package com.pathos.dev.animals.domain;

import java.util.List;

public class NotesRequest {

    private String interventionId;
    private List<Note> notes;

    public String getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(String interventionId) {
        this.interventionId = interventionId;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
