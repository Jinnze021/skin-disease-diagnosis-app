package com.project.demo.storage;

import com.project.demo.config.AppSecurityProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageStorageServiceTest {

    @TempDir
    Path uploadRoot;

    private ImageStorageService storage;

    @BeforeEach
    void setUp() {
        AppSecurityProperties properties = new AppSecurityProperties();
        properties.getUpload().setDir(uploadRoot.toString());
        properties.getUpload().setMaxBytes(5_242_880L);
        storage = new ImageStorageService(properties);
    }

    @Test
    void rejectsTraversalNameAndFakeImageContent() {
        MockMultipartFile fake = new MockMultipartFile(
                "file", "../../shell.jpg", "image/jpeg",
                "not-an-image".getBytes(StandardCharsets.UTF_8));

        assertThrows(InvalidImageException.class, () -> storage.store(fake));
    }

    @Test
    void storesValidPngUnderGeneratedName() throws Exception {
        StoredImage stored = storage.store(validOnePixelPng("same.png"));

        assertTrue(stored.getFileName().matches("[0-9a-f-]{36}\\.png"));
        assertFalse(stored.getFileName().contains("same"));
        assertTrue(Files.isRegularFile(stored.getPath()));
        assertTrue(Files.size(stored.getPath()) > 0);
        assertArrayEquals(Files.readAllBytes(stored.getPath()),
                Files.readAllBytes(storage.load(stored.getFileName()).getPath()));
    }

    @Test
    void rejectsOversizedAndUnsupportedFiles() {
        MockMultipartFile oversized = new MockMultipartFile(
                "file", "large.png", "image/png", new byte[5_242_881]);
        MockMultipartFile svg = new MockMultipartFile(
                "file", "image.svg", "image/svg+xml",
                "<svg xmlns='http://www.w3.org/2000/svg'/>".getBytes(StandardCharsets.UTF_8));

        assertThrows(InvalidImageException.class, () -> storage.store(oversized));
        assertThrows(InvalidImageException.class, () -> storage.store(svg));
    }

    @Test
    void refusesArbitraryReadNames() {
        assertThrows(InvalidImageException.class, () -> storage.load("../secret.png"));
        assertThrows(InvalidImageException.class, () -> storage.load("avatar.png"));
    }

    private MockMultipartFile validOnePixelPng(String name) {
        try {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(image, "png", output);
            return new MockMultipartFile("file", name, "image/png", output.toByteArray());
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
