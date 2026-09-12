package com.project.demo.controller;

import com.project.demo.storage.ImageStorageService;
import com.project.demo.storage.InvalidImageException;
import com.project.demo.storage.StoredImage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class UploadController {

    private static final String UUID_IMAGE_NAME =
            "{fileName:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.(?:png|jpg)}";

    private final ImageStorageService storage;

    public UploadController(ImageStorageService storage) {
        this.storage = storage;
    }

    @PostMapping({
            "/upload/image",
            "/user/upload",
            "/registered_user/upload",
            "/skin_detection/upload",
            "/hospital_information/upload",
            "/article/upload",
            "/article_type/upload",
            "/forum/upload",
            "/forum_type/upload",
            "/comment/upload",
            "/online_consultation/upload",
            "/knowledge_popularization/upload",
            "/slides/upload",
            "/nav/upload",
            "/user_group/upload",
            "/order/upload"
    })
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        StoredImage stored = storage.store(file);
        Map<String, Object> image = new LinkedHashMap<>();
        image.put("url", "/api/upload/files/" + stored.getFileName());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", image);
        return response;
    }

    @GetMapping("/upload/files/" + UUID_IMAGE_NAME)
    public ResponseEntity<Resource> read(@PathVariable String fileName) {
        try {
            StoredImage stored = storage.load(fileName);
            return ResponseEntity.ok()
                    .header("X-Content-Type-Options", "nosniff")
                    .contentType(MediaType.parseMediaType(stored.getContentType()))
                    .body(new FileSystemResource(stored.getPath()));
        } catch (InvalidImageException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
