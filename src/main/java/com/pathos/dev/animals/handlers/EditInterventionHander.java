package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pathos.dev.animals.domain.InterventionRequest;
import com.pathos.dev.animals.domain.InterventionsQueryResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditInterventionHander implements RequestHandler<InterventionRequest, InterventionsQueryResponse> {

    @Override
    public InterventionsQueryResponse handleRequest(InterventionRequest interventionRequest, Context context) {
        try {
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
            Map<String, AttributeValueUpdate> attributeValues = new HashMap<>();

            updateValWithS(attributeValues, "name", interventionRequest.getName());
            updateValWithS(attributeValues, "surname", interventionRequest.getSurname());
            updateValWithS(attributeValues, "description", interventionRequest.getDescription());
            updateValWithS(attributeValues, "phoneNumber", interventionRequest.getPhoneNumber());
            updateValWithS(attributeValues, "parcel", interventionRequest.getParcel());
            updateValWithS(attributeValues, "houseNumber", interventionRequest.getHouseNumber());
            updateValWithS(attributeValues, "city", interventionRequest.getCity());
            updateValWithS(attributeValues, "street", interventionRequest.getStreet());
            updateValWithN(attributeValues, "interventionStatus", interventionRequest.getRequestStatus());

            attributeValues.values().removeIf(Objects::isNull);

            UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                    .withTableName("Interventions")
                    .addKeyEntry("id", new AttributeValue().withS(interventionRequest.getId()))
                    .withAttributeUpdates(attributeValues);

            client.updateItem(updateItemRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new InterventionsQueryResponse();
    }

    private void updateValWithS(Map<String, AttributeValueUpdate> attributeValues, String key, String value) {
        if (value != null) {
            AttributeValueUpdate attributeValueUpdate = new AttributeValueUpdate();
            AttributeValue attributeValue = new AttributeValue().withS(value);
            attributeValueUpdate.setValue(attributeValue);
            attributeValues.put(key, attributeValueUpdate);
        }
    }

    private void updateValWithN(Map<String, AttributeValueUpdate> attributeValues, String key, int value) {
        AttributeValueUpdate attributeValueUpdate = new AttributeValueUpdate();
        AttributeValue attributeValue = new AttributeValue().withN(String.valueOf(value));
        attributeValueUpdate.setValue(attributeValue);
        attributeValues.put(key, attributeValueUpdate);
    }
}

