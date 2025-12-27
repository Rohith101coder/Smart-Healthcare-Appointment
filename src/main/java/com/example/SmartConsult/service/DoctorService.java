package com.example.SmartConsult.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.SmartConsult.dto.DoctorAppointmentResponse;
import com.example.SmartConsult.dto.DoctorRequest;
import com.example.SmartConsult.dto.DoctorResponse;
import com.example.SmartConsult.entity.Doctor;
import com.example.SmartConsult.entity.Role;
import com.example.SmartConsult.entity.User;
import com.example.SmartConsult.repository.AppointmentRepository;
import com.example.SmartConsult.repository.DoctorRepository;
import com.example.SmartConsult.repository.UserRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorService(DoctorRepository doctorRepository,
                         UserRepository userRepository,
                         AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // ADMIN ONLY
    public void addDoctor(DoctorRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.DOCTOR) {
            throw new RuntimeException("User is not a Doctor");
        }

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(request.getSpecialization());
        doctor.setExperience(request.getExperience());
        doctor.setConsultationFee(request.getConsultationFee());

        doctorRepository.save(doctor);
    }

    // PATIENT
    public List<DoctorResponse> getAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(doc -> {
                    DoctorResponse res = new DoctorResponse();
                    res.setId(doc.getId());
                    res.setName(doc.getUser().getName());
                    res.setSpecialization(doc.getSpecialization());
                    res.setExperience(doc.getExperience());
                    res.setConsultationFee(doc.getConsultationFee());
                    return res;
                })
                .collect(Collectors.toList());
    }

    // DOCTOR
    public List<DoctorAppointmentResponse> getDoctorAppointments() {

        // 1. Get logged-in doctor email
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        // 2. Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // 3. Get doctor profile
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        // 4. Get appointments
        return appointmentRepository.findByDoctorId(doctor.getId())
                .stream()
                .map(a -> {
                    DoctorAppointmentResponse res = new DoctorAppointmentResponse();
                    res.setAppointmentId(a.getId());
                    res.setPatientName(a.getPatient().getName());
                    res.setAppointmentDate(a.getAppointmentDate());
                    res.setTimeSlot(a.getTimeSlot());
                    res.setStatus(a.getStatus().name());
                    return res;
                })
                .collect(Collectors.toList());
    }
}
