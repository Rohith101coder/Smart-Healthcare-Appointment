package com.example.SmartConsult.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DoctorRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "Specialization cannot be blank")
    private String specialization;

    @Min(value = 0, message = "Experience cannot be negative")
    private int experience;

    @Positive(message = "Consultation fee must be positive")
    private double consultationFee;
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public int getExperience() {
        return experience;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public double getConsultationFee() {
        return consultationFee;
    }
    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }


    
}
