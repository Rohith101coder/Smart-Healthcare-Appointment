package com.example.SmartConsult.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartConsult.entity.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);
    List<MedicalRecord> findByAppointmentPatientId(Long patientId);
}
