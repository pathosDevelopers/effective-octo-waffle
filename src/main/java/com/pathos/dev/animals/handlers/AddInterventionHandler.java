package com.pathos.dev.animals.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pathos.dev.animals.domain.InterventionRequest;
import com.pathos.dev.animals.domain.InterventionResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class AddInterventionHandler implements RequestHandler<InterventionRequest, InterventionResponse> {

    @Override
    public InterventionResponse handleRequest(InterventionRequest interventionRequest, Context context) {

        String id = UUID.randomUUID().toString();
        InterventionResponse response = new InterventionResponse();

        try {
            getCoordinates(context, interventionRequest);
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

            context.getLogger().log("Input: " + interventionRequest.toString());

            interventionRequest.setId(id);
            interventionRequest.setRequestDate(new Date().getTime());

            DynamoDBMapper mapper = new DynamoDBMapper(client);

            mapper.save(interventionRequest);
            response.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private void getCoordinates(Context context, InterventionRequest interventionRequest) {
        try {
            String street = interventionRequest.getStreet();
            String number = interventionRequest.getHouseNumber();
            String city = interventionRequest.getCity();

            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + street + "+" + number + "&components=administrative_area:" + city + "|country:Poland&key=API_KEY");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output = br.lines().collect(Collectors.joining());
            context.getLogger().log("Output from Server .... \n");

            String latitude = output.substring(output.lastIndexOf("lat") + 6, output.lastIndexOf("lat") + 15);
            String longitude = output.substring(output.lastIndexOf("lng") + 6, output.lastIndexOf("lng") + 15);

            context.getLogger().log("coordinations: " + latitude + " " + longitude);
            interventionRequest.setLatitude(Double.parseDouble(latitude));
            interventionRequest.setLongitude(Double.parseDouble(longitude));

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
