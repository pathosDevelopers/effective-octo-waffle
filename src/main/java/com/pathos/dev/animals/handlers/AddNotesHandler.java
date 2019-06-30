package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.pathos.dev.animals.domain.InterventionRequest;
import com.pathos.dev.animals.domain.Note;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AddNotesHandler implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(inputStream);
        final Gson g = new Gson();

        JsonNode jsonId = json.get("interventionId");
        context.getLogger().log("InterventionId: " + jsonId);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        InterventionRequest intervention = mapper.load(InterventionRequest.class, jsonId.asText());


        JsonNode jsonNote = json.get("note");
        context.getLogger().log("note: " + jsonNote.get("title").asText());
        Note note = new Note();
        note.setTitle(jsonNote.get("title").asText());
        note.setAuthor(jsonNote.get("author").asText());
        note.setContent(jsonNote.get("content").asText());

        intervention.getNotes().add(note);

        mapper.save(intervention);
    }
}
