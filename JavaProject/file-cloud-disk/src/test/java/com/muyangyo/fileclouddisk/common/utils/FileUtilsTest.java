package com.muyangyo.fileclouddisk.common.utils;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/2
 * Time: 12:53
 */
public class FileUtilsTest {


//    private static final String TEST_DIR = "testDir";
//    private static final String TEST_FILE = "testFile.txt";
//    private static final String TEST_SUBDIR = "subDir";
//    private static final String TEST_SUBFILE = "subFile.txt";
//
//    @BeforeEach
//    void setUp() throws IOException {
//        // 创建测试目录和文件
//        Files.createDirectories(Paths.get(TEST_DIR));
//        Files.write(Paths.get(TEST_DIR, TEST_FILE), "Test content".getBytes());
//        Files.createDirectories(Paths.get(TEST_DIR, TEST_SUBDIR));
//        Files.write(Paths.get(TEST_DIR, TEST_SUBDIR, TEST_SUBFILE), "Sub file content".getBytes());
//    }
//
//    @AfterEach
//    void tearDown() throws IOException {
//        // 删除测试目录和文件
//        FileUtils.delete(TEST_DIR);
//    }
//
//    @Test
//    void testCopyFileToFile() throws IOException {
//        String targetPath = "targetFile.txt";
//        FileUtils.copy(Paths.get(TEST_DIR, TEST_FILE).toString(), targetPath);
//
//        assertTrue(Files.exists(Paths.get(targetPath)));
//        assertEquals("Test content", new String(Files.readAllBytes(Paths.get(targetPath))));
//
//        // 清理目标文件
//        Files.delete(Paths.get(targetPath));
//    }
//
//    @Test
//    void testCopyFileToDirectory() throws IOException {
//        String targetDir = "targetDir";
//        Files.createDirectories(Paths.get(targetDir));
//
//        FileUtils.copy(Paths.get(TEST_DIR, TEST_FILE).toString(), targetDir);
//
//        assertTrue(Files.exists(Paths.get(targetDir, TEST_FILE)));
//        assertEquals("Test content", new String(Files.readAllBytes(Paths.get(targetDir, TEST_FILE))));
//
//        // 清理目标目录
//        FileUtils.delete(targetDir);
//    }
//
//    @Test
//    void testCopyDirectoryToDirectory() throws IOException {
//        String targetDir = "targetDir";
//        FileUtils.copy(TEST_DIR, targetDir);
//
//        assertTrue(Files.exists(Paths.get(targetDir)));
//        assertTrue(Files.exists(Paths.get(targetDir, TEST_FILE)));
//        assertTrue(Files.exists(Paths.get(targetDir, TEST_SUBDIR)));
//        assertTrue(Files.exists(Paths.get(targetDir, TEST_SUBDIR, TEST_SUBFILE)));
//
//        assertEquals("Test content", new String(Files.readAllBytes(Paths.get(targetDir, TEST_FILE))));
//        assertEquals("Sub file content", new String(Files.readAllBytes(Paths.get(targetDir, TEST_SUBDIR, TEST_SUBFILE))));
//
//        // 清理目标目录
//        FileUtils.delete(targetDir);
//    }
//
//    @Test
//    void testCopyNonExistentFile() {
//        String nonExistentFile = "nonExistentFile.txt";
//        String targetPath = "targetFile.txt";
//
//        assertThrows(FileNotFoundException.class, () -> {
//            FileUtils.copy(nonExistentFile, targetPath);
//        });
//    }
//
//    @Test
//    void testCopyExistingFileWithOverwrite() throws IOException {
//        String targetPath = "targetFile.txt";
//        Files.write(Paths.get(targetPath), "Existing content".getBytes());
//
//        FileUtils.copy(Paths.get(TEST_DIR, TEST_FILE).toString(), targetPath);
//
//        assertTrue(Files.exists(Paths.get(targetPath)));
//        assertEquals("Test content", new String(Files.readAllBytes(Paths.get(targetPath))));
//
//        // 清理目标文件
//        Files.delete(Paths.get(targetPath));
//    }
//
//    @Test
//    void testCopyDirectoryWithExistingTarget() throws IOException {
//        String targetDir = "targetDir";
//        Files.createDirectories(Paths.get(targetDir));
//
//        FileUtils.copy(TEST_DIR, targetDir);
//
//        assertTrue(Files.exists(Paths.get(targetDir, TEST_FILE)));
//        assertTrue(Files.exists(Paths.get(targetDir, TEST_SUBDIR)));
//        assertTrue(Files.exists(Paths.get(targetDir, TEST_SUBDIR, TEST_SUBFILE)));
//
//        assertEquals("Test content", new String(Files.readAllBytes(Paths.get(targetDir, TEST_FILE))));
//        assertEquals("Sub file content", new String(Files.readAllBytes(Paths.get(targetDir, TEST_SUBDIR, TEST_SUBFILE))));
//
//        // 清理目标目录
//        FileUtils.delete(targetDir);
//    }
}
