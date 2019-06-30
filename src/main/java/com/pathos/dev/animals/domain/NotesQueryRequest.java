package com.pathos.dev.animals.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Interventions")
public class NotesQueryRequest {

    private String interventionId;

    @DynamoDBAttribute(attributeName = "interventionId")
    public String getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(String interventionId) {
        this.interventionId = interventionId;
    }
}
