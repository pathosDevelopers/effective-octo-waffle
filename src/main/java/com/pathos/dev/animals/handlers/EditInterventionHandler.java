package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pathos.dev.animals.domain.InterventionRequest;
import com.pathos.dev.animals.domain.InterventionsQueryResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditInterventionHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(inputStream);
        JsonNode jsonId = json.get("id");
        context.getLogger().log("InterventionId: " + jsonId);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        InterventionRequest intervention = mapper.load(InterventionRequest.class, jsonId.asText());

        JsonNode jsonIntervention = json.get("intervention");
        intervention.setCity(json.get("city").asText());
        intervention.setDescription(json.get("description").asText());
        intervention.setHouseNumber(json.get("houseNumber").asText());
        intervention.setName(json.get("name").asText());
        intervention.setParcel(json.get("parcel").asText());
        intervention.setPhoneNumber(json.get("phoneNumber").asText());
        intervention.setStreet(json.get("street").asText());
        intervention.setSurname(json.get("surname").asText());
        intervention.setRequestStatus(json.get("requestStatus").asInt());

        mapper.save(intervention);
    }

}

