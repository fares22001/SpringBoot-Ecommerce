package com.project.project.controller;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("COULD NOT SAVE IMAGE FILE " + fileName, ioe);
        }
    }

    public static void deleteFile(String uploadDir, String fileName) {
        Path uploadPath = Paths.get(uploadDir);
        if (Files.exists(uploadPath)) {
            Path filePath = uploadPath.resolve(fileName);
            if (Files.exists(filePath)) {
                try {
                    Files.delete(filePath);
                } catch (IOException ioe) {
                    throw new RuntimeException("COULD NOT DELETE IMAGE FILE " + fileName, ioe);
                }
            }
        }
    }
}