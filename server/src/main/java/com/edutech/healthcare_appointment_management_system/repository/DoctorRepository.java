package com.edutech.healthcare_appointment_management_system.repository;

import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM Doctor d JOIN d.appointments a WHERE a.patient.id = :patientId")
    List<Doctor> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT d FROM Doctor d WHERE d.id = :doctorId")
    Doctor findByDoctorId(@Param("doctorId") Long doctorId);
}
