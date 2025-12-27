package com.example.SmartConsult.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartConsult.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Optional<Doctor> findByUserId(Long userId);
}
