package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pathos.dev.animals.domain.Note;
import com.pathos.dev.animals.domain.NotesRequest;
import com.pathos.dev.animals.domain.NotesResponse;

import java.util.*;

public class AddNotesHandler implements RequestHandler<NotesRequest, NotesResponse> {
    @Override
    public NotesResponse handleRequest(NotesRequest notesRequest, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

        for (Note note : notesRequest.getNotes()) {
            Map<String, AttributeValue> attributeValues = new HashMap<>();
            attributeValues.put("id", new AttributeValue().withS(UUID.randomUUID().toString()));
            attributeValues.put("requestDate", new AttributeValue().withN(Long.toString(new Date().getTime())));

            addValWithS(attributeValues, "interventionId", notesRequest.getInterventionId());
            addValWithS(attributeValues, "title", note.getTitle());
            addValWithS(attributeValues, "content", note.getContent());
            addValWithS(attributeValues, "author", note.getAuthor());

            attributeValues.values().removeIf(Objects::isNull);

            PutItemRequest putItemRequest = new PutItemRequest().withTableName("Interventions").withItem(attributeValues);
            client.putItem(putItemRequest);
        }

        return new NotesResponse();
    }

    private void addValWithS(Map<String, AttributeValue> attributeValues, String key, String value) {
        if (value != null) {
            AttributeValue attributeValue = new AttributeValue().withS(value);
            attributeValues.put(key, attributeValue);
        }
    }
}
