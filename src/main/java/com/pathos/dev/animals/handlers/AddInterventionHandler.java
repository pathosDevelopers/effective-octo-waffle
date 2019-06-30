package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
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

            interventionRequest.setId(UUID.randomUUID().toString());
            interventionRequest.setRequestDate(new Date().getTime());

            DynamoDBMapper mapper = new DynamoDBMapper(client);

            mapper.save(interventionRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new InterventionsQueryResponse();
    }
}
