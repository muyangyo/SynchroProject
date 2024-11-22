package com.example.chfs.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

@RestController
public class PreviewController {

    @RequestMapping(value = "/previewDocx", method = RequestMethod.GET)
    public void previewDocx(HttpServletResponse response) throws IOException {
        // 获取文件的二进制流
        File docxFile = new File("C:\\Users\\DR\\Desktop\\tmp1\\分散实习申请表(1).docx");
        try (InputStream inputStream = Files.newInputStream(docxFile.toPath());
             OutputStream outputStream = response.getOutputStream()) {

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setContentLength((int) docxFile.length());

            // 将文件写入响应流
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }
    }

    @GetMapping("/previewImage")
    public void previewImage(HttpServletResponse response) {
        String filePath = "C:\\Users\\DR\\Desktop\\Pixpin_2024-11-22_15-24-17.png"; // 本地图片路径
        File file = new File(filePath);

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("image/jpeg"); // 根据图片类型设置Content-Type
        response.setHeader("Content-Disposition", "inline; filename=image.jpg");

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @GetMapping("/previewAudio")
    public void previewAudio(HttpServletResponse response) {
        String filePath = "C:\\Users\\DR\\Desktop\\tmp1\\多糖芒果冰-葛雨晴.320.mp3"; // 本地音频文件路径
        File file = new File(filePath);

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("audio/mpeg"); // 根据音频类型设置Content-Type
        response.setHeader("Content-Disposition", "inline; filename=audio.mp3");

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @GetMapping("/previewPdf")
    public void previewPdf(HttpServletResponse response) {
        String filePath = "C:\\Users\\DR\\Desktop\\tmp1\\00-Vue3阶段必会的前置知识.pdf"; // 本地PDF文件路径
        File file = new File(filePath);

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/pdf"); // 根据PDF类型设置Content-Type
        response.setHeader("Content-Disposition", "inline; filename=file.pdf");

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }


    @GetMapping("/previewTxt")
    public void getFile(HttpServletResponse response) {
        // 设置响应头
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "inline; filename=\"" + "filename.txt" + "\"");

        // 读取本地文件
        File file = new File("C:\\Users\\DR\\Desktop\\tmp1\\FileController.java");
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
