package com.example.chfs.service;

import com.example.chfs.model.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    @Value("${file.storage.path}")
    private String[] storagePaths;

    public List<FileInfo> listFiles() {
        List<FileInfo> fileInfos = new ArrayList<>();
        for (String path : storagePaths) {
            File directory = new File(path);
            if (directory.exists() && directory.isDirectory()) {
                listFilesRecursively(directory, fileInfos);
            }
        }
        return fileInfos;
    }

    private void listFilesRecursively(File directory, List<FileInfo> fileInfos) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                fileInfos.add(new FileInfo(file.getName(), file.getPath(), -1)); // -1 indicates a directory
                listFilesRecursively(file, fileInfos);
            } else {
                fileInfos.add(new FileInfo(file.getName(), file.getPath(), file.length()));
            }
        }
    }

    public void uploadFile(MultipartFile file, String directory) throws IOException {
        Path path = Paths.get(directory, file.getOriginalFilename());
        file.transferTo(path);
    }

    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public void renameFile(String oldFilePath, String newFileName) {
        File oldFile = new File(oldFilePath);
        if (oldFile.exists()) {
            File newFile = new File(oldFile.getParent(), newFileName);
            oldFile.renameTo(newFile);
        }
    }

    public byte[] downloadFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            return Files.readAllBytes(file.toPath());
        }
        return null;
    }

    public void createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public void createFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        Files.write(file.toPath(), content.getBytes());
    }
}