package com.example.chfs.controller;

import com.example.chfs.model.FileInfo;
import com.example.chfs.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/files")
//@CrossOrigin(origins = "http://localhost:5173")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    public ResponseEntity<List<FileInfo>> listFiles() {
        return ResponseEntity.ok(fileService.listFiles());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("directory") String directory) {
        try {
            fileService.uploadFile(file, directory);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("filePath") String filePath) {
        fileService.deleteFile(filePath);
        return ResponseEntity.ok("File deleted successfully.");
    }

    @PutMapping("/rename")
    public ResponseEntity<String> renameFile(@RequestParam("oldFilePath") String oldFilePath, @RequestParam("newFileName") String newFileName) {
        fileService.renameFile(oldFilePath, newFileName);
        return ResponseEntity.ok("File renamed successfully.");
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("filePath") String filePath) {
        try {
            byte[] data = fileService.downloadFile(filePath);
            if (data != null) {
                String encodedFileName = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
                ByteArrayResource resource = new ByteArrayResource(data);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodedFileName)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(data.length)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createFolder")
    public ResponseEntity<String> createFolder(@RequestParam("folderPath") String folderPath) {
        fileService.createFolder(folderPath);
        return ResponseEntity.ok("Folder created successfully.");
    }

    @PostMapping("/createFile")
    public ResponseEntity<String> createFile(@RequestParam("filePath") String filePath, @RequestParam("content") String content) {
        try {
            fileService.createFile(filePath, content);
            return ResponseEntity.ok("File created successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create file.");
        }
    }
}