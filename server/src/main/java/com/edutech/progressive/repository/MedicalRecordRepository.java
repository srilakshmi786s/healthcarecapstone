package com.edutech.progressive.repository;

import com.edutech.progressive.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
    List<MedicalRecord> findByAppointment_Patient_PatientId(Integer patientId);
    MedicalRecord findByAppointment_AppointmentId(Integer appointmentId);
}
