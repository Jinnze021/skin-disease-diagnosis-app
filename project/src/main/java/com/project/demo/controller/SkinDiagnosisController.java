package com.project.demo.controller;

import com.project.demo.easydl.DiagnosisResult;
import com.project.demo.easydl.EasyDlDisabledException;
import com.project.demo.easydl.EasyDlClient;
import com.project.demo.easydl.EasyDlUnavailableException;
import com.project.demo.storage.ImageStorageService;
import com.project.demo.storage.InvalidImageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class SkinDiagnosisController {

    private final EasyDlClient client;
    private final ImageStorageService imageStorage;

    public SkinDiagnosisController(EasyDlClient client, ImageStorageService imageStorage) {
        this.client = client;
        this.imageStorage = imageStorage;
    }

    @PostMapping("/ai/skin-diagnosis")
    public ResponseEntity<Map<String, Object>> diagnose(@RequestParam("file") MultipartFile file) {
        try {
            DiagnosisResult diagnosis = client.diagnose(imageStorage.validate(file));
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("name", diagnosis.getName());
            result.put("score", diagnosis.getScore());
            return ResponseEntity.ok(envelope("result", result));
        } catch (InvalidImageException | IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(error(40000, exception.getMessage()));
        } catch (EasyDlDisabledException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(error(50300, "诊断服务尚未配置"));
        } catch (EasyDlUnavailableException exception) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(error(50200, "诊断服务暂不可用，请稍后重试"));
        }
    }

    private Map<String, Object> error(int code, String message) {
        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("code", code);
        detail.put("message", message);
        return envelope("error", detail);
    }

    private Map<String, Object> envelope(String key, Object value) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(key, value);
        return response;
    }
}
