package com.healthexpert.healthbot.repository;

import com.healthexpert.healthbot.entity.PatientCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientCaseRepository extends JpaRepository<PatientCase,Long> {
    Optional<PatientCase> findByPhoneNumber(String phoneNumber);
    // fetch the latest case for a phone number
    Optional<PatientCase> findTopByPhoneNumberOrderByCreatedAtDesc(String phoneNumber);
}
