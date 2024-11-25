package com.muyangyo.fileclouddisk.common.model.enums;

import com.muyangyo.fileclouddisk.common.utils.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// 文件详细类型枚举
public enum SubdividedFileType {
    // 视频类型
    VIDEO_MP4("MP4", "video/mp4", Collections.singletonList("mp4"), FileCategory.VIDEO),
    VIDEO_AVI("AVI", "video/x-msvideo", Collections.singletonList("avi"), FileCategory.VIDEO),
    VIDEO_MKV("MKV", "video/x-matroska", Collections.singletonList("mkv"), FileCategory.VIDEO),
    VIDEO_MOV("MOV", "video/quicktime", Collections.singletonList("mov"), FileCategory.VIDEO),
    VIDEO_WMV("WMV", "video/x-ms-wmv", Collections.singletonList("wmv"), FileCategory.VIDEO),

    // 文本类型
    TEXT_PLAIN("Plain Text", "text/plain", Collections.singletonList("txt"), FileCategory.TEXT),
    TEXT_HTML("HTML", "text/html", Arrays.asList("html", "htm"), FileCategory.TEXT),
    TEXT_CSS("CSS", "text/css", Collections.singletonList("css"), FileCategory.TEXT),
    TEXT_JAVASCRIPT("JavaScript", "application/javascript", Collections.singletonList("js"), FileCategory.TEXT),
    TEXT_CSV("CSV", "text/csv", Collections.singletonList("csv"), FileCategory.TEXT),
    TEXT_JAVA("Java", "text/plain", Collections.singletonList("java"), FileCategory.TEXT),
    TEXT_C("C", "text/plain", Collections.singletonList("c"), FileCategory.TEXT),
    TEXT_PYTHON("Python", "text/plain", Collections.singletonList("py"), FileCategory.TEXT),

    // PDF 类型
    PDF("PDF", "application/pdf", Collections.singletonList("pdf"), FileCategory.PDF),

    // DOCX 类型
    DOCX("DOCX", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            Collections.singletonList("docx"), FileCategory.DOCX),

    // 音频类型
    AUDIO_MP3("MP3", "audio/mpeg", Collections.singletonList("mp3"), FileCategory.AUDIO),
    AUDIO_WAV("WAV", "audio/wav", Collections.singletonList("wav"), FileCategory.AUDIO),
    AUDIO_FLAC("FLAC", "audio/flac", Collections.singletonList("flac"), FileCategory.AUDIO),
    AUDIO_AAC("AAC", "audio/aac", Collections.singletonList("aac"), FileCategory.AUDIO),
    AUDIO_OGG("OGG", "audio/ogg", Collections.singletonList("ogg"), FileCategory.AUDIO),

    // 图片类型
    IMAGE_JPG("JPEG", "image/jpeg", Arrays.asList("jpg", "jpeg"), FileCategory.IMAGE),
    IMAGE_PNG("PNG", "image/png", Collections.singletonList("png"), FileCategory.IMAGE),
    IMAGE_GIF("GIF", "image/gif", Collections.singletonList("gif"), FileCategory.IMAGE),
    IMAGE_BMP("BMP", "image/bmp", Collections.singletonList("bmp"), FileCategory.IMAGE),
    IMAGE_ICO("ICO", "image/x-icon", Collections.singletonList("ico"), FileCategory.IMAGE),
    IMAGE_JFIF("JFIF", "image/jpeg", Collections.singletonList("jfif"), FileCategory.IMAGE),
    IMAGE_WEBP("WEBP", "image/webp", Collections.singletonList("webp"), FileCategory.IMAGE),

    // 文件夹
    FOLDER("Folder", "inode/directory", Collections.singletonList("folder"), FileCategory.FOLDER),

    // 未知类型
    UNKNOWN("Unknown", "application/octet-stream", Collections.singletonList("unknown"), FileCategory.UNKNOWN);

    private final String typeName;
    private final String mimeType;
    private final List<String> extensions;
    private final FileCategory category;

    private SubdividedFileType(String typeName, String mimeType, List<String> extensions, FileCategory category) {
        this.typeName = typeName;
        this.mimeType = mimeType;
        this.extensions = extensions;
        this.category = category;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getMimeType() {
        return mimeType;
    }

    private List<String> getExtensions() {
        return extensions;
    }

    public FileCategory getCategory() {
        return category;
    }

    /**
     * 根据文件名获取文件详细类型
     *
     * @param file 文件
     * @return SubdividedFileType 文件详细类型
     */
    public static SubdividedFileType fromFileName(File file) {
        if (file.isDirectory()) {
            return FOLDER;
        }

        String extension = FileUtils.getFileExtension(file);
        for (SubdividedFileType type : values()) {
            if (type.getExtensions().contains(extension)) {
                return type;
            }
        }
        return UNKNOWN;
    }

}