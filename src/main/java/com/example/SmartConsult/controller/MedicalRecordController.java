package com.example.SmartConsult.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartConsult.dto.MedicalRecordRequest;
import com.example.SmartConsult.dto.MedicalRecordResponse;
import com.example.SmartConsult.service.MedicalRecordService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // DOCTOR: Create medical record for an appointment
    @PostMapping("/doctor/medical-records/{appointmentId}")
    public ResponseEntity<String> createMedicalRecord(
            @PathVariable Long appointmentId,
            @Valid @RequestBody MedicalRecordRequest request) {
        
        medicalRecordService.createMedicalRecord(appointmentId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Medical record created successfully");
    }

    // PATIENT: Get their medical records
    @GetMapping("/patient/medical-records")
    public ResponseEntity<List<MedicalRecordResponse>> getMyMedicalRecords() {
        return ResponseEntity.ok(medicalRecordService.getMyMedicalRecords());
    }

    // DOCTOR: Get their created medical records
    @GetMapping("/doctor/medical-records")
    public ResponseEntity<List<MedicalRecordResponse>> getMyCreatedRecords() {
        return ResponseEntity.ok(medicalRecordService.getMyCreatedRecords());
    }
}
