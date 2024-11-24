package com.example.chfs.controller;

import com.example.chfs.model.FileForList;
import com.example.chfs.service.DirService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/getFiles")
@Slf4j
public class DirController {
    @Autowired
    private DirService dirService;


    @GetMapping("/{path}")
    public List<FileForList> getFiles(@PathVariable String path) {
        log.info(path);
        return dirService.listFiles(path);
    }
}