package com.edutech.progressive.repository;

import com.edutech.progressive.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Integer> {
    List<DoctorAvailability> findByDoctor_DoctorId(int doctorId);
    List<DoctorAvailability> findByDoctor_DoctorIdAndAvailableDate(int doctorId, Date date);
}
