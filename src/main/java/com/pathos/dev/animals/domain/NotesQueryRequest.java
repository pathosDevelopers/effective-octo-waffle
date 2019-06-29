package com.pathos.dev.animals.domain;

import java.util.List;

public class NotesQueryRequest {

    private String id;
    private List<Note> notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> nottes) {
        this.notes = nottes;
    }
}
