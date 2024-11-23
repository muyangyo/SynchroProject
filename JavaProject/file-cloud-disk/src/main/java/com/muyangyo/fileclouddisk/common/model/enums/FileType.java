package com.muyangyo.fileclouddisk.common.model.enums;

import com.muyangyo.fileclouddisk.common.utils.FileUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum FileType {
    // 视频类型
    VIDEO_MP4("MP4", "video/mp4", Collections.singletonList("mp4")),
    VIDEO_AVI("AVI", "video/x-msvideo", Collections.singletonList("avi")),
    VIDEO_MKV("MKV", "video/x-matroska", Collections.singletonList("mkv")),
    VIDEO_MOV("MOV", "video/quicktime", Collections.singletonList("mov")),
    VIDEO_WMV("WMV", "video/x-ms-wmv", Collections.singletonList("wmv")),

    // 文本类型
    TEXT_PLAIN("Plain Text", "text/plain", Collections.singletonList("txt")),
    TEXT_HTML("HTML", "text/html", Arrays.asList("html", "htm")),
    TEXT_CSS("CSS", "text/css", Collections.singletonList("css")),
    TEXT_JAVASCRIPT("JavaScript", "application/javascript", Collections.singletonList("js")),
    TEXT_CSV("CSV", "text/csv", Collections.singletonList("csv")),
    TEXT_JAVA("Java", "text/plain", Collections.singletonList("java")),
    TEXT_C("C", "text/plain", Collections.singletonList("c")),
    TEXT_PYTHON("Python", "text/plain", Collections.singletonList("py")),

    // PDF 类型
    PDF("PDF", "application/pdf", Collections.singletonList("pdf")),

    // DOCX 类型
    DOCX("DOCX", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            Collections.singletonList("docx")),

    // 音频类型
    AUDIO_MP3("MP3", "audio/mpeg", Collections.singletonList("mp3")),
    AUDIO_WAV("WAV", "audio/wav", Collections.singletonList("wav")),
    AUDIO_FLAC("FLAC", "audio/flac", Collections.singletonList("flac")),
    AUDIO_AAC("AAC", "audio/aac", Collections.singletonList("aac")),
    AUDIO_OGG("OGG", "audio/ogg", Collections.singletonList("ogg")),

    // 图片类型
    IMAGE_JPG("JPEG", "image/jpeg", Arrays.asList("jpg", "jpeg")),
    IMAGE_PNG("PNG", "image/png", Collections.singletonList("png")),
    IMAGE_GIF("GIF", "image/gif", Collections.singletonList("gif")),
    IMAGE_BMP("BMP", "image/bmp", Collections.singletonList("bmp")),
    IMAGE_ICO("ICO", "image/x-icon", Collections.singletonList("ico")),
    IMAGE_JFIF("JFIF", "image/jpeg", Collections.singletonList("jfif")),
    IMAGE_WEBP("WEBP", "image/webp", Collections.singletonList("webp"));

    private final String typeName;
    private final String mimeType;
    private final List<String> extensions;

    private FileType(String typeName, String mimeType, List<String> extensions) {
        this.typeName = typeName;
        this.mimeType = mimeType;
        this.extensions = extensions;
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

    // 通过文件名获取文件类型
    public static FileType fromFileName(String fileName) {
        if (!StringUtils.hasLength(fileName)) {
            return null;
        }
        String extension = FileUtils.getFileExtension(fileName);
        for (FileType type : values()) {
            if (type.getExtensions().contains(extension)) {
                return type;
            }
        }
        return null;
    }


    public static final FileType[] VIDEO_TYPES = {
            VIDEO_MP4, VIDEO_AVI, VIDEO_MKV, VIDEO_MOV, VIDEO_WMV
    };

    public static final FileType[] TEXT_TYPES = {
            TEXT_PLAIN, TEXT_HTML, TEXT_CSS, TEXT_JAVASCRIPT, TEXT_CSV,
            TEXT_JAVA, TEXT_C, TEXT_PYTHON
    };

    public static final FileType[] PDF_TYPES = {
            PDF
    };

    public static final FileType[] DOCX_TYPES = {
            DOCX
    };

    public static final FileType[] AUDIO_TYPES = {
            AUDIO_MP3, AUDIO_WAV, AUDIO_FLAC, AUDIO_AAC, AUDIO_OGG
    };

    public static final FileType[] IMAGE_TYPES = {
            IMAGE_JPG, IMAGE_PNG, IMAGE_GIF, IMAGE_BMP, IMAGE_ICO,
            IMAGE_JFIF, IMAGE_WEBP
    };
}