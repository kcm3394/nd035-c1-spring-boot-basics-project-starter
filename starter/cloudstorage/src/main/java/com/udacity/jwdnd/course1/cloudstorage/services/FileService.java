package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFilesByUserId(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public void uploadFile(User user, MultipartFile fileUpload) {
        try {
            InputStream inputStream = fileUpload.getInputStream();
            byte[] data = inputStream.readAllBytes();
            String contentType = fileUpload.getContentType();
            String size = String.valueOf(fileUpload.getSize());
            String name = fileUpload.getName();

            File file = new File(null, name, contentType, size, user.getUserId(), data);
            this.fileMapper.uploadFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
