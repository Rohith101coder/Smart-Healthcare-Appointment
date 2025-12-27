package com.example.SmartConsult.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.SmartConsult.dto.AppointmentRequest;
import com.example.SmartConsult.dto.AppointmentResponse;
import com.example.SmartConsult.entity.Appointment;
import com.example.SmartConsult.entity.AppointmentStatus;
import com.example.SmartConsult.entity.Doctor;
import com.example.SmartConsult.entity.User;
import com.example.SmartConsult.repository.AppointmentRepository;
import com.example.SmartConsult.repository.DoctorRepository;
import com.example.SmartConsult.repository.UserRepository;

@Service
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    
    // PATIENT
    public void bookAppointment(AppointmentRequest request) {

        // 1. Get logged-in patient email
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 2. Check doctor exists
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // 3. Prevent double booking
        boolean exists = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndTimeSlot(
                        doctor.getId(),
                        request.getAppointmentDate(),
                        request.getTimeSlot()
                );

        if (exists) {
           return;
        }

        // 4. Create appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setTimeSlot(request.getTimeSlot());
        appointment.setStatus(AppointmentStatus.BOOKED);

        appointmentRepository.save(appointment);
    }

    // PATIENT
    public List<AppointmentResponse> myAppointments() {

        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return appointmentRepository.findAll()
                .stream()
                .filter(a -> a.getPatient().getId().equals(patient.getId()))
                .map(a -> {
                    AppointmentResponse res = new AppointmentResponse();
                    res.setId(a.getId());
                    res.setDoctorName(a.getDoctor().getUser().getName());
                    res.setAppointmentDate(a.getAppointmentDate());
                    res.setTimeSlot(a.getTimeSlot());
                    res.setStatus(a.getStatus().name());
                    return res;
                })
                .collect(Collectors.toList());
    }

}
