package com.example.chfs.controller;

import com.example.chfs.config.NonStaticResourceHttpRequestHandler;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/files")
//@CrossOrigin(origins = "http://localhost:*")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    public void video(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "C:\\Users\\DR\\Desktop\\26118915590-1-192.mp4";
        File file = new File(path);
        if (file.exists()) {
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, path);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }


//    @GetMapping("/video")
//    public void getSampleVideo(HttpServletResponse response) {
//        File videoFile = new File("C:\\Users\\DR\\Desktop\\26118915590-1-192.mp4");
//
//        if (!videoFile.exists()) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            return;
//        }
//
//        response.setContentType("video/mp4");
//        response.setContentLength((int) videoFile.length());
//
//        try (FileInputStream fileInputStream = new FileInputStream(videoFile);
//             OutputStream outputStream = response.getOutputStream()) {
//
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//        } catch (IOException e) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }


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
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodedFileName).contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(data.length).body(resource);
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