package com.example.SmartConsult.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.SmartConsult.dto.MedicalRecordRequest;
import com.example.SmartConsult.dto.MedicalRecordResponse;
import com.example.SmartConsult.entity.Appointment;
import com.example.SmartConsult.entity.MedicalRecord;
import com.example.SmartConsult.entity.User;
import com.example.SmartConsult.exception.ResourceNotFoundException;
import com.example.SmartConsult.exception.UnauthorizedException;
import com.example.SmartConsult.repository.AppointmentRepository;
import com.example.SmartConsult.repository.MedicalRecordRepository;
import com.example.SmartConsult.repository.UserRepository;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                AppointmentRepository appointmentRepository,
                                UserRepository userRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    // DOCTOR: Create medical record after appointment
    public void createMedicalRecord(Long appointmentId, MedicalRecordRequest request) {
        
        // Get logged-in doctor
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User doctor = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // Get appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Check if logged-in user is the doctor for this appointment
        if (!appointment.getDoctor().getUser().getId().equals(doctor.getId())) {
            throw new UnauthorizedException("You can only create records for your own appointments");
        }

        // Check if record already exists
        if (medicalRecordRepository.findByAppointmentId(appointmentId).isPresent()) {
            throw new ResourceNotFoundException("Medical record already exists for this appointment");
        }

        // Create medical record
        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setDiagnosis(request.getDiagnosis());
        record.setPrescription(request.getPrescription());
        record.setNotes(request.getNotes());

        medicalRecordRepository.save(record);
    }

    // PATIENT: Get own medical records
    public List<MedicalRecordResponse> getMyMedicalRecords() {
        
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return medicalRecordRepository.findByAppointmentPatientId(patient.getId())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // DOCTOR: Get medical records they created
    public List<MedicalRecordResponse> getMyCreatedRecords() {
        
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User doctor = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return medicalRecordRepository.findAll()
                .stream()
                .filter(record -> record.getAppointment().getDoctor().getUser().getId().equals(doctor.getId()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private MedicalRecordResponse convertToResponse(MedicalRecord record) {
        MedicalRecordResponse res = new MedicalRecordResponse();
        res.setId(record.getId());
        res.setAppointmentId(record.getAppointment().getId());
        res.setDiagnosis(record.getDiagnosis());
        res.setPrescription(record.getPrescription());
        res.setNotes(record.getNotes());
        res.setPatientName(record.getAppointment().getPatient().getName());
        res.setDoctorName(record.getAppointment().getDoctor().getUser().getName());
        return res;
    }
}
