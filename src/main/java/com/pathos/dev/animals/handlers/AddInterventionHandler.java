package com.pathos.dev.animals.handlers;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pathos.dev.animals.domain.InterventionRequest;

import java.io.*;

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

            AmazonDynamoDBClient client = new AmazonDynamoDBClient(new DefaultAWSCredentialsProviderChain());
            DynamoDB dynamoDB = new DynamoDB(client);
            Table table = dynamoDB.getTable("intervention");
            Item item = new Item();
            
            if(event.get("body") != null){
                InterventionRequest request = new InterventionRequest(event.get("body"));
            }
            responseBodyJson.addProperty("message", "New item created");

            headerJson.addProperty("x-custom-header", "my custom header value");
            responseJson.add("headers", headerJson);
            responseJson.add("body", responseBodyJson);

        }catch (Exception e){
            e.printStackTrace();
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }
}
