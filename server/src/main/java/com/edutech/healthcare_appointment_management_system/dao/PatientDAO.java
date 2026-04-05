package com.edutech.healthcare_appointment_management_system.dao;

import com.edutech.healthcare_appointment_management_system.entity.Patient;

import java.sql.SQLException;
import java.util.List;

public interface PatientDAO {
    int addPatient(Patient patient)throws SQLException;
    Patient getPatientById(int patientId)throws SQLException;
    void updatePatient (Patient patient)throws SQLException;
    void deletePatient (int patientId)throws SQLException;
    List<Patient> getAllPatients()throws SQLException;
}