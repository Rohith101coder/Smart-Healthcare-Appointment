package com.example.SmartConsult.dto;

import jakarta.validation.constraints.NotBlank;

public class MedicalRecordRequest {
    @NotBlank(message = "Diagnosis cannot be blank")
    private String diagnosis;

    @NotBlank(message = "Prescription cannot be blank")
    private String prescription;

    private String notes;

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
