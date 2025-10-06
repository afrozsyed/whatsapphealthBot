package com.healthexpert.healthbot.service;

import com.healthexpert.healthbot.entity.HealthCenter;
import com.healthexpert.healthbot.repository.HealthCenterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCenterService {

    private final HealthCenterRepository repository;

    public HealthCenterService(HealthCenterRepository repository) {
        this.repository = repository;
    }

    public List<HealthCenter> insertSampleCenters() {
        HealthCenter center1 = new HealthCenter(null, "City Hospital", "Dr. Smith", "Dr. Jane",
                "560001", "9876543210", "9876543211", "cityhospital@example.com");

        HealthCenter center2 = new HealthCenter(null, "Sunshine Clinic", "Dr. Rahul", "Dr. Priya",
                "560002", "9876543220", "9876543221", "sunshine@example.com");

        HealthCenter center3 = new HealthCenter(null, "Apollo Health", "Dr. Kumar", "Dr. Neha",
                "560003", "9876543230", "9876543231", "apollo@example.com");

        return repository.saveAll(List.of(center1, center2, center3));
    }

    public List<HealthCenter> getAllCenters() {
        return repository.findAll();
    }
}
