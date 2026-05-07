package com.example.SmartConsult.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartConsult.dto.AppointmentRequest;
import com.example.SmartConsult.dto.AppointmentResponse;
import com.example.SmartConsult.service.AppointmentService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments")
    public ResponseEntity<String> bookAppointment(
            @Valid @RequestBody AppointmentRequest request) {

        appointmentService.bookAppointment(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Appointment booked successfully");
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> myAppointments() {
        return ResponseEntity.ok(appointmentService.myAppointments());
    }

    @DeleteMapping("/appointments/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable String appointmentId) {
        appointmentService.cancelAppointment(Long.parseLong(appointmentId));
        return ResponseEntity.ok("Appointment cancelled successfully");
    }
}
