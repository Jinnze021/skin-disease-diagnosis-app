package com.project.demo.controller;

import com.project.demo.controller.base.BaseController;
import com.project.demo.entity.HospitalInformation;
import com.project.demo.service.HospitalInformationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hospital_information")
public class HospitalInformationController
        extends BaseController<HospitalInformation, HospitalInformationService> {

    public HospitalInformationController(HospitalInformationService service) {
        setService(service);
    }

    /**
     * Returns a deterministic, parameterized fallback list. The legacy endpoint
     * executed client-influenced SQL and has intentionally been retired.
     */
    @GetMapping("/get_hits_list")
    public Map<String, Object> getHits() {
        return success(service.latest(12));
    }
}
