package com.pathos.dev.animals.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class AS3BeanProvider {
    private static final String BUCKET_NAME = "interventionsfiles";
    private static final String ACCESS_KEY = "ACCESS_KEY";
    private static final String SECRET_KEY = "SECRET_KEY";

    private static final AmazonS3 s3Client;

    static {
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public static AmazonS3 getInstance() {
        return s3Client;
    }

    public static GeneratePresignedUrlRequest generatePresignedUrlReques (String filename) {
        return new GeneratePresignedUrlRequest(BUCKET_NAME, filename);
    }
}
