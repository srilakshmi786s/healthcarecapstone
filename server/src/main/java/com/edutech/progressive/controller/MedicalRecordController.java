package com.edutech.progressive.controller;

import com.edutech.progressive.dto.MedicalRecordDTO;
import com.edutech.progressive.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/patient/medicalrecords")
    public ResponseEntity<List<MedicalRecordDTO>> getPatientMedicalRecords(@RequestParam("patientId") Integer patientId) {
        List<MedicalRecordDTO> records = medicalRecordService.getPatientRecords(patientId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PostMapping("/doctor/medicalrecords")
    public ResponseEntity<?> saveOrUpdateMedicalRecord(
            @RequestBody MedicalRecordDTO dto,
            @RequestParam("doctorId") Integer doctorId) {
        try {
            MedicalRecordDTO saved = medicalRecordService.saveOrUpdateRecord(dto, doctorId);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
    
    @GetMapping("/doctor/medicalrecords/{appointmentId}")
    public ResponseEntity<?> getRecordByAppointment(@PathVariable Integer appointmentId) {
        MedicalRecordDTO record = medicalRecordService.getRecordByAppointment(appointmentId);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }
}
