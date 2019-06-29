package com.pathos.dev.animals.handlers;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
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

            if (event.get("body") != null) {
                InterventionRequest request = new InterventionRequest(event.get("body"));

                Item item = new Item()
                        .with("creationDate", new AttributeValue().withS(request.getCreationDate().toString()))
                        .with("mofificationDate", new AttributeValue().withS(request.getMofificationDate().toString()))
                        .with("name", new AttributeValue().withS(request.getName()))
                        .with("surname", new AttributeValue().withS(request.getSurname()))
                        .with("description", new AttributeValue().withS(request.getDescription()))
                        .with("phoneNumber", new AttributeValue().withS(request.getPhoneNumber()))
                        .with("parcel", new AttributeValue().withS(request.getParcel()))
                        .with("houseNumber", new AttributeValue().withS(request.getHouseNumber()))
                        .with("city", new AttributeValue().withS(request.getCity()))
                        .with("street", new AttributeValue().withS(request.getStreet()));

                PutItemSpec itemSpec = new PutItemSpec()
                        .withItem(item)
                        .withConditionExpression("attribute_not_exists(#ps)")
                        .withNameMap(new NameMap()
                                .with("#ps", "PartitionSpec"));

                dynamoDB.getTable("intervention").putItem(itemSpec);

            }
            responseBodyJson.addProperty("message", "new intervention created");

            headerJson.addProperty("x-custom-header", "my custom header value");
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
