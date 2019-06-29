package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pathos.dev.animals.domain.InterventionRequest;
import com.pathos.dev.animals.domain.InterventionsQueryRequest;
import com.pathos.dev.animals.domain.InterventionsQueryResponse;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.stream.Collectors;

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

        DynamoDBQueryExpression<InterventionRequest> queryExpression = new DynamoDBQueryExpression<InterventionRequest>()
                .withKeyConditionExpression("id = :id")
                .withExpressionAttributeValues(attributesMap);

        InterventionsQueryResponse response = new InterventionsQueryResponse();

        DynamoDBMapper mapper = new DynamoDBMapper(client);
        if(id != null) {
            PaginatedQueryList<InterventionRequest> queryResult = mapper.query(InterventionRequest.class, queryExpression);
            response.count = queryResult.size();
            response.interventions = queryResult.stream().collect(Collectors.toList());
        } else {
            ScanResult scanResult = client.scan(scanRequest);
            response.count = scanResult.getCount();
//            response.interventions = scanResult.getItems();
        }




        return response;
    }
}
