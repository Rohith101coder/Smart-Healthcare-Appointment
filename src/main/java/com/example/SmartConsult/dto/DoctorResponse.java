package com.example.SmartConsult.dto;

public class DoctorResponse {
    private Long id;
    private String name;
    private String specialization;
    private int experience;
    private double consultationFee;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public void setConsultationFee(double cosultationFee) {
        this.consultationFee = cosultationFee;
    }

    
}
