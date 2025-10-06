package com.healthexpert.healthbot.service;

import com.healthexpert.healthbot.entity.HealthCenter;
import com.healthexpert.healthbot.entity.PatientCase;
import com.healthexpert.healthbot.repository.HealthCenterRepository;
import com.healthexpert.healthbot.repository.PatientCaseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WhatsAppBotService {

    private final PatientCaseRepository patientRepo;
    private final HealthCenterRepository healthCenterRepo;
    private final TwilioService twilloService;

    public WhatsAppBotService(PatientCaseRepository patientRepo, HealthCenterRepository healthCenterRepo, TwilioService twilloService) {
        this.patientRepo = patientRepo;
        this.healthCenterRepo = healthCenterRepo;
        this.twilloService = twilloService;
    }

    public void handleIncommingMessage(String from, String body) {
        System.out.println("+++++ inside handleIncommingMessage+++++");
        String phone = from.replace("whatsapp:", "");
        // Fetch the latest patient case for this phone
        Optional<PatientCase> optionalPatient = patientRepo.findTopByPhoneNumberOrderByCreatedAtDesc(phone);

        if (optionalPatient.isEmpty()) {
            // New patient - create a case and ask for name
            PatientCase newPatient = new PatientCase();
            newPatient.setPhoneNumber(phone);
            newPatient.setStage("ASK_NAME");
            patientRepo.save(newPatient);
            sendServiceMessage(phone,  "Hi! Welcome to HealthBot. Please tell me your name:");
            return; // STOP here, wait for user's reply
        }
        PatientCase patient = optionalPatient.get();
        System.out.println("+++++handleIncommingMessage patientStage+++++"+patient.getStage());

        switch (patient.getStage()) {
            case "ASK_NAME" -> askName(patient, body, phone);
            case "ASK_AGE" -> askAge(patient, body, phone);
            case "ASK_ADDRESS" -> askAddress(patient, body, phone);
            case "ASK_SYMPTOMS" -> askSymptoms(patient, body, phone);
            case "COMPLETED" -> completeAndRestart(patient, body, phone);
        }
    }

    private void completeAndRestart(PatientCase patient, String body, String phone) {
        System.out.println("+++++ inside completeAndRestart+++++");
        // Close old case
        patient.setStage("COMPLETED");
        patientRepo.save(patient);

        // Create new case for this patient (fresh entry)
        PatientCase newCase = new PatientCase();
        newCase.setPhoneNumber(phone);
        newCase.setStage("ASK_NAME");
        patientRepo.save(newCase);
        sendServiceMessage(phone,  "We’ve recorded your last case ✅. Let's start a new case.\n" +
                "Please tell me your name:");
    }

    private void askSymptoms(PatientCase patient, String body, String phone) {
        System.out.println("+++++ inside askSymptoms+++++");
        patient.setSymptoms(body.trim());
        patient.setStage("COMPLETED");
        patientRepo.save(patient);

        // Find nearest health center
        HealthCenter center = healthCenterRepo.findByZipCode(patient.getZipCode())
                .orElseThrow(() -> new RuntimeException("No health center found for zip " + patient.getZipCode()));

        // Notify patient
        sendServiceMessage(phone, "Thank you. Your case has been assigned to " + center.getPrimaryContactName() +
                ". They will contact you shortly.");

        // Notify health center
        sendServiceMessage(center.getPrimaryContactNumber(), "New Patient Case:\n" +
                "Name: " + patient.getName() + "\n" +
                "Age: " + patient.getAge() + "\n" +
                "Zip: " + patient.getZipCode() + "\n" +
                "Symptoms: " + patient.getSymptoms() + "\n" +
                "Patient WhatsApp: " + phone);
    }

    private void askAddress(PatientCase patient, String body, String phone) {
        System.out.println("+++++ inside askPinCode+++++");
        patient.setAddress(body.trim());
        // Extract pincode from address (simple approach: look for last 6 digits)
        String pincode = extractPincode(body);
        if(pincode == null){
            sendServiceMessage(phone, "We could not find PinCode in your Address, Please provide Pin");
            return;
        }
        System.out.println("++++++pincode+++++"+pincode);
        patient.setZipCode(pincode);

        patient.setStage("ASK_SYMPTOMS");
        patientRepo.save(patient);
        sendServiceMessage(phone, "Got it. Please describe your symptoms:");
    }

    private void askAge(PatientCase patient, String body, String phone) {
        System.out.println("+++++ inside askAge+++++");
        try {
            patient.setAge(Integer.parseInt(body.trim()));
            patient.setStage("ASK_ADDRESS");
            patientRepo.save(patient);
            sendServiceMessage(phone, "Great! Please share your Address with pincode:");
        } catch (NumberFormatException e) {
            sendServiceMessage(phone, "Age must be a number. Please re-enter your age:");
        }
    }

    private void askName(PatientCase patient, String body, String phone) {
        System.out.println("+++++ inside askName+++++");
        patient.setName(body.trim());
        patient.setStage("ASK_AGE");
        patientRepo.save(patient);
        sendServiceMessage(phone, "Thanks, " + patient.getName() + ". Please enter your age:");
    }

    private void sendServiceMessage(String phone, String message) {
        System.out.println("+++++ inside sendServiceMessage+++++");
        System.out.println("+++++++++++++++++"+message);
        twilloService.sendMessage(phone,message);

    }

    private String extractPincode(String address) {
        Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
        Matcher matcher = pattern.matcher(address);
        if (matcher.find()) {
            return matcher.group();
        }
        return null; // if not found, you may ask patient again
    }

}
