package com.project.demo.controller;

import com.project.demo.easydl.DiagnosisResult;
import com.project.demo.easydl.EasyDlClient;
import com.project.demo.easydl.EasyDlUnavailableException;
import com.project.demo.storage.ImageStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class SkinDiagnosisControllerTest {

    @Test
    void controllerReturnsOnlyNormalizedNameAndScore() throws Exception {
        EasyDlClient client = mock(EasyDlClient.class);
        ImageStorageService storage = mock(ImageStorageService.class);
        byte[] png = new byte[]{(byte) 0x89, 0x50, 0x4e, 0x47};
        when(storage.validate(any())).thenReturn(png);
        when(client.diagnose(png)).thenReturn(new DiagnosisResult("eczema", 0.91d));
        MockMvc mockMvc = standaloneSetup(new SkinDiagnosisController(client, storage)).build();
        MockMultipartFile image = new MockMultipartFile("file", "skin.png", "image/png", png);

        mockMvc.perform(multipart("/ai/skin-diagnosis").file(image))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.name").value("eczema"))
                .andExpect(jsonPath("$.result.score").value(0.91d))
                .andExpect(jsonPath("$.result.access_token").doesNotExist())
                .andExpect(jsonPath("$.access_token").doesNotExist());
    }

    @Test
    void upstreamFailureBecomesSanitizedBadGateway() throws Exception {
        EasyDlClient client = mock(EasyDlClient.class);
        ImageStorageService storage = mock(ImageStorageService.class);
        when(storage.validate(any())).thenReturn(new byte[]{1});
        when(client.diagnose(any())).thenThrow(new EasyDlUnavailableException("upstream body secret"));
        MockMvc mockMvc = standaloneSetup(new SkinDiagnosisController(client, storage)).build();
        MockMultipartFile image = new MockMultipartFile("file", "skin.png", "image/png", new byte[]{1});

        mockMvc.perform(multipart("/ai/skin-diagnosis").file(image))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.error.message").value("诊断服务暂不可用，请稍后重试"));
    }
}
