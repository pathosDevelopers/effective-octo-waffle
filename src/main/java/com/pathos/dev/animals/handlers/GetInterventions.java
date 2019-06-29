package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.pathos.dev.animals.domain.InterventionRequest;
import com.pathos.dev.animals.domain.InterventionsQueryRequest;
import com.pathos.dev.animals.domain.InterventionsQueryResponse;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GetInterventions implements RequestHandler {

    private static final String TABLE_NAME = "Interventions";
    private static final Integer CLOSED_INTERVENTION_STATUS = 4;

    @Override
    public Object handleRequest(Object o, Context context) {
        String id = null;
//        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//        StringBuilder responseStrBuilder = new StringBuilder();
//
//        String inputStr;
//        while ((inputStr = streamReader.readLine()) != null) {
//            responseStrBuilder.append(inputStr);
//            System.out.println(inputStr);
//        }
//        JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
//        try {
//            if (jsonObject.get("pathParameters") != null) {
//                JSONObject pps = (JSONObject) jsonObject.get("pathParameters");
//                if (pps.get("id") != null) {
//                    id = (String) pps.get("id");
//                }
//            }
//        } catch (JSONException exc) {
//            System.out.println("Path parameter not found");
//        }

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

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

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
            PaginatedScanList<InterventionRequest> scanResult = mapper.scan(InterventionRequest.class, scanExpression);
            response.count = scanResult.size();
            response.interventions = scanResult.stream().collect(Collectors.toList());
        }

//        outputStream.write(new ObjectMapper().writeValueAsString(response).getBytes());
//        outputStream.close();

        String responseString = null;
        try {
            responseString = new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return responseString;
    }
}
