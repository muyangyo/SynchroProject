package com.example.raftdemo.service;

import com.example.raftdemo.model.FileChunk;
import com.example.raftdemo.model.FileMetadata;
import com.example.raftdemo.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/9
 * Time: 9:43
 */
@Service
public class FileService {

    @Autowired
    private RaftService raftService;

    @Autowired
    private FileRepository fileRepository;

    public void uploadFile(MultipartFile file, String uploader) throws IOException {
        String fileId = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        Date uploadTime = new Date();
        String checksum = calculateChecksum(file.getBytes());

        FileMetadata metadata = new FileMetadata(fileId, fileName, fileSize, uploader, uploadTime, checksum);
        raftService.replicateCommand("uploadMetadata", metadata);

        int chunkSize = 1024 * 1024; // 1MB
        byte[] fileData = file.getBytes();
        int totalChunks = (int) Math.ceil((double) fileSize / chunkSize);

        for (int i = 0; i < totalChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, fileSize);
            byte[] chunkData = Arrays.copyOfRange(fileData, start, end);
            FileChunk chunk = new FileChunk(fileId, i, chunkData);
            raftService.replicateCommand("uploadChunk", chunk);
        }
    }

    private String calculateChecksum(byte[] data) {
        // 计算文件的校验和
        return DigestUtils.md5Hex(data);
    }
}