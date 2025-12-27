package com.example.SmartConsult.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartConsult.dto.DoctorAppointmentResponse;
import com.example.SmartConsult.dto.DoctorRequest;
import com.example.SmartConsult.dto.DoctorResponse;
import com.example.SmartConsult.service.DoctorService;

@RestController
@RequestMapping("/api")
public class DoctorController {
    
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    //ADMIN
    @PostMapping("/admin/doctors")
    public ResponseEntity<String> addDoctor(@RequestBody DoctorRequest request){
        doctorService.addDoctor(request);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body("Doctor added successfull");
    }

    //PATIENT
    @GetMapping("/patient/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctors(){
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/doctor/appointments")
public ResponseEntity<List<DoctorAppointmentResponse>> myAppointments() {
    return ResponseEntity.ok(doctorService.getDoctorAppointments());
}

}
