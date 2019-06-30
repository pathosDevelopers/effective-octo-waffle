package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pathos.dev.animals.domain.Note;
import com.pathos.dev.animals.domain.NotesQueryResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GetNotesHandler implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        String id;
        context.getLogger().log("Input: " + inputStream);
        final ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(inputStream);

        context.getLogger().log("interventionId: " + json.path("interventionId"));
        id = json.path("interventionId").asText();

        HashMap<String, AttributeValue> attributesMap = new HashMap<>();
        attributesMap.put(":interventionId", new AttributeValue().withS(id));

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

        DynamoDBQueryExpression<Note> queryExpression = new DynamoDBQueryExpression<Note>()
                .withKeyConditionExpression("id = :id")
                .withExpressionAttributeValues(attributesMap);

        NotesQueryResponse queryResponse = new NotesQueryResponse();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        PaginatedQueryList<Note> queryResult = mapper.query(Note.class, queryExpression);
        queryResponse.setCount(queryResponse.getCount());
        queryResponse.setNotes(queryResult.stream().collect(Collectors.toList()));

        outputStream.write(new ObjectMapper().writeValueAsString(queryResponse).getBytes());
        outputStream.close();
    }
}
