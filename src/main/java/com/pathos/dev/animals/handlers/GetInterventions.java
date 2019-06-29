package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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

public class GetInterventions implements RequestStreamHandler {

    private static final String TABLE_NAME = "Interventions";
    private static final Integer CLOSED_INTERVENTION_STATUS = 4;

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        String id = null;
        context.getLogger().log("Input: " + inputStream);
        final ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(inputStream);

        JsonNode jsonId = json.path("id");
        context.getLogger().log("Id: " + jsonId);
        if(jsonId != null) {
            id = jsonId.asText();
        }

        HashMap<String, AttributeValue> attributesMap = new HashMap<>();
        if (id != null) {
            attributesMap.put(":id", new AttributeValue().withS(id));
        } else {
            attributesMap.put(":interventionDate", new AttributeValue().withN(String.valueOf(LocalDate.now().minusDays(30).toEpochDay())));
        }

        ScanRequest scanRequest = new ScanRequest().withTableName(TABLE_NAME);

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

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

        outputStream.write(new ObjectMapper().writeValueAsString(response).getBytes());
        outputStream.close();

    }
}
