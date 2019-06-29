package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pathos.dev.animals.domain.InterventionRequest;
import com.pathos.dev.animals.domain.InterventionsQueryResponse;

import java.util.*;

public class AddInterventionHandler implements RequestHandler<InterventionRequest, InterventionsQueryResponse> {

    @Override
    public InterventionsQueryResponse handleRequest(InterventionRequest interventionRequest, Context context) {

        try {
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
            Map<String, AttributeValue> attributeValues = new HashMap<>();

            attributeValues.put("id", new AttributeValue().withS(UUID.randomUUID().toString()));

            attributeValues.put("requestDate", new AttributeValue().withN(Long.toString(new Date().getTime())));

            if (interventionRequest.getName() != null)
                attributeValues.put("name", new AttributeValue().withS(interventionRequest.getName()));
            if (interventionRequest.getSurname() != null)
                attributeValues.put("surname", new AttributeValue().withS(interventionRequest.getSurname()));
            if (interventionRequest.getDescription() != null)
                attributeValues.put("description", new AttributeValue().withS(interventionRequest.getDescription()));
            if (interventionRequest.getPhoneNumber() != null)
                attributeValues.put("phoneNumber", new AttributeValue().withS(interventionRequest.getPhoneNumber()));
            if (interventionRequest.getParcel() != null)
                attributeValues.put("parcel", new AttributeValue().withS(interventionRequest.getParcel()));
            if (interventionRequest.getHouseNumber() != null)
                attributeValues.put("houseNumber", new AttributeValue().withS(interventionRequest.getHouseNumber()));
            if (interventionRequest.getCity() != null)
                attributeValues.put("city", new AttributeValue().withS(interventionRequest.getCity()));
            if (interventionRequest.getStreet() != null)
                attributeValues.put("street", new AttributeValue().withS(interventionRequest.getStreet()));
            attributeValues.put("interventionStatus", new AttributeValue().withN(String.valueOf(interventionRequest.getRequestStatus())));

            attributeValues.values().removeIf(Objects::isNull);

            PutItemRequest putItemRequest = new PutItemRequest().withTableName("Interventions").withItem(attributeValues);
            client.putItem(putItemRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new InterventionsQueryResponse();
    }
}
