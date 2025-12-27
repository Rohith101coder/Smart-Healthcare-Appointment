package com.example.SmartConsult.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorAppointmentResponse {
    
    private Long appointmentId;
    private String patientName;
    private LocalDate appointmentDate;
    private LocalTime timeSlot;
    private String status;
    public Long getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public LocalTime getTimeSlot() {
        return timeSlot;
    }
    public void setTimeSlot(LocalTime timeSlot) {
        this.timeSlot = timeSlot;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    
}
