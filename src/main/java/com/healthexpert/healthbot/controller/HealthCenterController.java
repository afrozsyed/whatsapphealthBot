package com.healthexpert.healthbot.controller;

import com.healthexpert.healthbot.entity.HealthCenter;
import com.healthexpert.healthbot.service.HealthCenterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/healthcenters")
public class HealthCenterController {
    private final HealthCenterService hsService;

    public HealthCenterController( HealthCenterService hsService) {
        this.hsService = hsService;
    }

    // Insert sample data into H2
    @PostMapping("/insert-sample")
    public List<HealthCenter> insertSampleData() {
        return hsService.insertSampleCenters();
    }

    // Fetch all data
    @GetMapping
    public List<HealthCenter> getAllCenters() {
        return hsService.getAllCenters();
    }
}
