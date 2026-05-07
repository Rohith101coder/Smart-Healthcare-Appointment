package com.example.SmartConsult.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public class AppointmentRequest {
    @NotNull(message = "Doctor ID cannot be null")
    private Long doctorId;

    @NotNull(message = "Appointment date cannot be null")
    @FutureOrPresent(message = "Appointment date must be today or in the future")
    private LocalDate appointmentDate;

    @NotNull(message = "Time slot cannot be null")
    private LocalTime timeSlot;
    public Long getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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

    
}
