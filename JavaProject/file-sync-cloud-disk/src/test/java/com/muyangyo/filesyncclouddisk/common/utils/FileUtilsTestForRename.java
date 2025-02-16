package com.muyangyo.filesyncclouddisk.common.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileUtilsTestForRename {

    private static final String TEST_DIR = "testDir";
    private static final String TEST_FILE = "testFile.txt";
    private static final String TEST_FILE_CONTENT = "Hello, World!";

    @BeforeEach
    public void setUp() throws IOException {
        // 创建测试目录和测试文件
        Files.createDirectories(Paths.get(TEST_DIR));
        Files.write(Paths.get(TEST_DIR, TEST_FILE), TEST_FILE_CONTENT.getBytes());
    }

    @AfterEach
    public void tearDown() throws IOException {
        // 删除测试目录及其内容
        FileUtils.delete(TEST_DIR);
    }

    @Test
    public void testRenameFile() throws IOException {
        String oldPath = TEST_DIR + File.separator + TEST_FILE;
        String newName = "renamedFile.txt";
        String newPath = TEST_DIR + File.separator + newName;

        // 重命名文件
        boolean success = FileUtils.rename(oldPath, newName);
        assertTrue(success);

        // 检查新文件是否存在
        assertTrue(Files.exists(Paths.get(newPath)));
        // 检查原文件是否不存在
        assertFalse(Files.exists(Paths.get(oldPath)));
    }

    @Test
    public void testRenameFileWithSameName() throws IOException {
        String oldPath = TEST_DIR + File.separator + TEST_FILE;
        String newName = TEST_FILE;

        // 重命名文件
        boolean success = FileUtils.rename(oldPath, newName);
        assertTrue(success);

        // 检查文件是否仍然存在
        assertTrue(Files.exists(Paths.get(oldPath)));
    }

    @Test
    public void testRenameFileWithExistingName() throws IOException {
        String oldPath = TEST_DIR + File.separator + TEST_FILE;
        String newName = "existingFile.txt";
        String newPath = TEST_DIR + File.separator + newName;

        // 创建一个同名文件
        Files.write(Paths.get(newPath), "Existing content".getBytes());

        // 重命名文件
        boolean success = FileUtils.rename(oldPath, newName);
        assertTrue(success);

        // 检查新文件是否存在
        assertTrue(Files.exists(Paths.get(newPath)));
        // 检查原文件是否不存在
        assertFalse(Files.exists(Paths.get(oldPath)));

        // 检查新文件名是否是唯一的
        String uniqueFileName = FileUtils.getFileName(newPath);
        assertTrue(uniqueFileName.startsWith(newName));
    }

    @Test
    public void testRenameDirectory() throws IOException {
        String oldDirPath = TEST_DIR + File.separator + "oldDir";
        String newDirName = "newDir";
        String newDirPath = TEST_DIR + File.separator + newDirName;

        // 创建测试目录
        Files.createDirectories(Paths.get(oldDirPath));

        // 重命名目录
        boolean success = FileUtils.rename(oldDirPath, newDirName);
        assertTrue(success);

        // 检查新目录是否存在
        assertTrue(Files.exists(Paths.get(newDirPath)));
        // 检查原目录是否不存在
        assertFalse(Files.exists(Paths.get(oldDirPath)));
    }

    @Test
    public void testRenameNonExistentFile() {
        String oldPath = TEST_DIR + File.separator + "nonExistentFile.txt";
        String newName = "newFile.txt";

        // 重命名不存在的文件
        Exception exception = assertThrows(IOException.class, () -> {
            FileUtils.rename(oldPath, newName);
        });

        // 检查异常信息
        String expectedMessage = "文件或目录不存在: " + oldPath;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}