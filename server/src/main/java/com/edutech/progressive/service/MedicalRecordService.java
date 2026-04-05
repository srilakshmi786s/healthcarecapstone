package com.edutech.progressive.service;

import com.edutech.progressive.entity.MedicalRecord;
import com.edutech.progressive.repository.MedicalRecordRepository;
import com.edutech.progressive.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        return medicalRecordRepository.findAll().stream()
                .filter(mr -> mr.getPatient() != null && mr.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }
}
