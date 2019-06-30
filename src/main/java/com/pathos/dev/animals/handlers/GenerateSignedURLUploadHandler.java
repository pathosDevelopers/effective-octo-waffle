package com.pathos.dev.animals.handlers;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.pathos.dev.animals.configuration.AS3BeanProvider;
import com.pathos.dev.animals.domain.UrlRequest;

public class GenerateSignedURLUploadHandler implements RequestHandler<UrlRequest, String> {


    @Override
    public String handleRequest(UrlRequest urlFilename, Context context) {
        java.util.Date expiration = new java.util.Date();
        long msec = expiration.getTime();
        msec += 1000 * 60 * 2; // 2 minutes
        expiration.setTime(msec);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = AS3BeanProvider.generatePresignedUrlReques(urlFilename.filename);
        generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
        generatePresignedUrlRequest.setExpiration(expiration);

        return AS3BeanProvider.getInstance().generatePresignedUrl(generatePresignedUrlRequest).toString();
    }
}
