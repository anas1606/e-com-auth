package com.example.authentication.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

public class FileUpload {

    public String saveFile(String folder,MultipartFile multipartFile, String id) throws IOException {
        try {
            String ext = getFileExtension(multipartFile.getOriginalFilename());
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(folder + id + "." + ext);
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: ");
        }
    }

    public static String getFileExtension(String fileName) {

        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

}
