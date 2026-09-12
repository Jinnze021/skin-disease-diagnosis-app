package com.project.demo.storage;

import java.nio.file.Path;

public class StoredImage {
    private final String fileName;
    private final String contentType;
    private final Path path;

    public StoredImage(String fileName, String contentType, Path path) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public Path getPath() {
        return path;
    }
}
