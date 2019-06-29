package com.pathos.dev.animals.domain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.List;
import java.util.Map;

public class NotesQueryResponse {

    public Integer count;
    public List<String> notes;

}
