package com.muyangyo.fileclouddisk.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/30
 * Time: 0:04
 */
class FileUtilsTestForExtension {

    @Test
    public void testGetFileExtension_NormalFileWithExtension() {
        String filePath = "/path/to/file.txt";
        String expectedExtension = "txt";
        String actualExtension = FileUtils.getFileExtension(filePath);
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    public void testGetFileExtension_NormalFileWithoutExtension() {
        String filePath = "/path/to/file";
        String expectedExtension = "";
        String actualExtension = FileUtils.getFileExtension(filePath);
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    public void testGetFileExtension_FileWithMultipleDots() {
        String filePath = "/path/to/file.with.multiple.dots.txt";
        String expectedExtension = "txt";
        String actualExtension = FileUtils.getFileExtension(filePath);
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    public void testGetFileExtension_EmptyFilePath() {
        String filePath = "";
        String expectedExtension = "";
        String actualExtension = FileUtils.getFileExtension(filePath);
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    public void testGetFileExtension_NullFilePath() {
        String filePath = null;
        assertThrows(NullPointerException.class, () -> {
            FileUtils.getFileExtension(filePath);
        });
    }

    @Test
    public void testGetFileExtension_FilePathWithSpecialCharacters() {
        String filePath = "/path/to/file!@#$%^&*().txt";
        String expectedExtension = "txt";
        String actualExtension = FileUtils.getFileExtension(filePath);
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    public void testGetFileExtension_FilePathWithNoExtension() {
        String filePath = "/path/to/file.";
        String expectedExtension = "";
        String actualExtension = FileUtils.getFileExtension(filePath);
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    public void testGetFileExtension_FilePathWithDotAtStart() {
        String filePath = "/path/to/.file.txt";
        String expectedExtension = "txt";
        String actualExtension = FileUtils.getFileExtension(filePath);
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    public void testGetFileExtension_FilePathWithDotAtEnd() {
        String filePath = "/path/to/file.txt.";
        String expectedExtension = "";
        String actualExtension = FileUtils.getFileExtension(filePath);
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    public void testGetFileExtension_FilePathWithMultipleDotsAtEnd() {
        String filePath = "/path/to/file.txt..";
        String expectedExtension = "";
        String actualExtension = FileUtils.getFileExtension(filePath);
        assertEquals(expectedExtension, actualExtension);
    }
}