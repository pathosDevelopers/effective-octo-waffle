package com.pathos.dev.animals.handlers;

import com.pathos.dev.animals.service.UploadClient;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileHandler {

    public static void main(String[] args) {
        UploadClient uploadClient = new UploadClient();

        Path path = Paths.get("/home/lbujak/file.txt");
        String name = "file.txt";
        String originalFileName = "file.txt";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);

        uploadClient.uploadFile(result);
    }
}
