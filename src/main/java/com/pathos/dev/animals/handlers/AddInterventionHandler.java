package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pathos.dev.animals.domain.InterventionRequest;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddInterventionHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        JsonParser parser = new JsonParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        JsonObject responseJson = new JsonObject();
        JsonObject responseBodyJson = new JsonObject();
        JsonObject headerJson = new JsonObject();

        try {
            JsonObject event = (JsonObject) parser.parse(reader);
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

            String id = null;
            if (event.get("body") != null) {
                InterventionRequest request = new InterventionRequest(event.get("body"));
                Map<String, AttributeValue> attributeValues = new HashMap<>();

                id = UUID.randomUUID().toString();

                attributeValues.put("id", new AttributeValue().withS(UUID.randomUUID().toString()));

                attributeValues.put("requestDate", new AttributeValue().withN(id));
                attributeValues.put("creationDate", new AttributeValue().withN(Long.toString(new Date().getTime() + 100)));
                attributeValues.put("mofificationDate", new AttributeValue().withN(Long.toString(new Date().getTime() + 100)));

                attributeValues.put("name", new AttributeValue().withS(request.getName()));
                attributeValues.put("surname", new AttributeValue().withS(request.getSurname()));
                attributeValues.put("description", new AttributeValue().withS(request.getDescription()));
                attributeValues.put("phoneNumber", new AttributeValue().withS(request.getPhoneNumber()));
                attributeValues.put("parcel", new AttributeValue().withS(request.getParcel()));
                attributeValues.put("houseNumber", new AttributeValue().withS(request.getHouseNumber()));
                attributeValues.put("city", new AttributeValue().withS(request.getCity()));
                attributeValues.put("street", new AttributeValue().withS(request.getStreet()));
                attributeValues.put("interventionStatus", new AttributeValue().withS(String.valueOf(request.getStatus())));

                PutItemRequest putItemRequest = new PutItemRequest().withTableName("Interventions").withItem(attributeValues);
                client.putItem(putItemRequest);

            }
            responseBodyJson.addProperty("message", "new intervention created " + id);

            headerJson.addProperty("x-custom-header", "OK");
            responseJson.add("headers", headerJson);
            responseJson.add("body", responseBodyJson);

        } catch (Exception e) {
            e.printStackTrace();
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }
}
