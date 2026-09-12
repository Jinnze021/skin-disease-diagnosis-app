package com.project.demo.controller;

import com.project.demo.storage.ImageStorageService;
import com.project.demo.storage.StoredImage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UploadControllerTest {

    private static final String FILE_NAME = "123e4567-e89b-12d3-a456-426614174000.png";

    @TempDir
    Path root;

    @Test
    void returnsOnlyGeneratedPublicUrl() throws Exception {
        ImageStorageService storage = mock(ImageStorageService.class);
        Path path = root.resolve(FILE_NAME);
        when(storage.store(any())).thenReturn(new StoredImage(FILE_NAME, "image/png", path));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new UploadController(storage)).build();
        MockMultipartFile file = new MockMultipartFile(
                "file", "../../avatar.png", "image/png", new byte[]{1});

        mockMvc.perform(multipart("/upload/image").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.url").value("/api/upload/files/" + FILE_NAME));
    }

    @Test
    void servesOnlyValidatedNamesWithNoSniffHeader() throws Exception {
        ImageStorageService storage = mock(ImageStorageService.class);
        Path path = root.resolve(FILE_NAME);
        byte[] contentBytes = new byte[]{1, 2, 3};
        Files.write(path, contentBytes);
        when(storage.load(FILE_NAME)).thenReturn(new StoredImage(FILE_NAME, "image/png", path));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new UploadController(storage)).build();

        mockMvc.perform(get("/upload/files/" + FILE_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(content().bytes(contentBytes));

        mockMvc.perform(get("/upload/files/avatar.png"))
                .andExpect(status().isNotFound());
    }
}
