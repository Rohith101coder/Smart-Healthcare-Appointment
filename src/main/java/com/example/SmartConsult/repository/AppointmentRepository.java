package com.example.SmartConsult.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartConsult.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {



    boolean existsByDoctorIdAndAppointmentDateAndTimeSlot(
        Long doctorId,
        LocalDate appointmentDate,
        LocalTime timeSlot
    );

    List<Appointment> findByDoctorId(Long doctorId);
}
