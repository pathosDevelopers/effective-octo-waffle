package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pathos.dev.animals.domain.InterventionsQueryRequest;
import com.pathos.dev.animals.domain.InterventionsQueryResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class GetInterventions implements RequestHandler<InterventionsQueryRequest, InterventionsQueryResponse> {

    private static final String TABLE_NAME = "Interventions";
    private static final Integer CLOSED_INTERVENTION_STATUS = 4;

    @Override
    public InterventionsQueryResponse handleRequest(InterventionsQueryRequest input, Context context) {
        String id = input.id;

        HashMap<String, AttributeValue> attributesMap = new HashMap<>();
        if (id != null) {
            attributesMap.put(":id", new AttributeValue().withS(id));
        } else {
            attributesMap.put(":interventionDate", new AttributeValue().withN(String.valueOf(LocalDate.now().minusDays(30).toEpochDay())));
        }

        ScanRequest scanRequest = new ScanRequest().withTableName(TABLE_NAME)
//                .withFilterExpression("interventionDate < :interventionDate")
//                .withExpressionAttributeValues(attributesMap)
                ;

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withKeyConditionExpression("id = :id")
                .withExpressionAttributeValues(attributesMap);

        InterventionsQueryResponse response = new InterventionsQueryResponse();

        if(id != null) {
            response.count = client.query(queryRequest).getCount();
        } else {
            response.count = client.scan(scanRequest).getCount();
        }

        return response;
    }
}
