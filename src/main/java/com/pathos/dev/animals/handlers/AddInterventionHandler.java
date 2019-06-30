package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pathos.dev.animals.domain.InterventionRequest;
import com.pathos.dev.animals.domain.InterventionsQueryResponse;

import java.util.*;

public class AddInterventionHandler implements RequestHandler<InterventionRequest, InterventionsQueryResponse> {

    @Override
    public InterventionsQueryResponse handleRequest(InterventionRequest interventionRequest, Context context) {

        try {
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
            Map<String, AttributeValue> attributeValues = new HashMap<>();

            context.getLogger().log("Input: " + interventionRequest.toString());
            final ObjectMapper objectMapper = new ObjectMapper();

            attributeValues.put("id", new AttributeValue().withS(UUID.randomUUID().toString()));
            attributeValues.put("requestDate", new AttributeValue().withN(Long.toString(new Date().getTime())));

            addValWithS(attributeValues, "name", interventionRequest.getName());
            addValWithS(attributeValues, "surname", interventionRequest.getSurname());
            addValWithS(attributeValues, "description", interventionRequest.getDescription());
            addValWithS(attributeValues, "phoneNumber", interventionRequest.getPhoneNumber());
            addValWithS(attributeValues, "parcel", interventionRequest.getParcel());
            addValWithS(attributeValues, "houseNumber", interventionRequest.getHouseNumber());
            addValWithS(attributeValues, "city", interventionRequest.getCity());
            addValWithS(attributeValues, "street", interventionRequest.getStreet());
            addValWithN(attributeValues, "requestStatus", interventionRequest.getRequestStatus());

            attributeValues.values().removeIf(Objects::isNull);

            PutItemRequest putItemRequest = new PutItemRequest().withTableName("Interventions").withItem(attributeValues);
            client.putItem(putItemRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new InterventionsQueryResponse();
    }

    private void addValWithS(Map<String, AttributeValue> attributeValues, String key, String value) {
        if (value != null) {
            AttributeValue attributeValue = new AttributeValue().withS(value);
            attributeValues.put(key, attributeValue);
        }
    }

    private void addValWithN(Map<String, AttributeValue> attributeValues, String key, int value) {
            AttributeValue attributeValue = new AttributeValue().withN(String.valueOf(value));
            attributeValues.put(key, attributeValue);
    }
}
