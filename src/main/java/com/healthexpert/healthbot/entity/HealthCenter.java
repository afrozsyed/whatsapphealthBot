package com.healthexpert.healthbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String centerLocation;
    private String primaryContactName;
    private String secondaryContactName;
    private String zipCode;
    private String primaryContactNumber;
    private String secondaryContactNumber;
    private String email;
}
