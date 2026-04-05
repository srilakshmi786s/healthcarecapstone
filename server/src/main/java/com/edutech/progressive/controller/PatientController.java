package com.edutech.progressive.controller;

import com.edutech.progressive.entity.Appointment;
import com.edutech.progressive.entity.Doctor;
import com.edutech.progressive.entity.MedicalRecord;
import com.edutech.progressive.service.AppointmentService;
import com.edutech.progressive.service.DoctorService;
import com.edutech.progressive.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController {

    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;
    private final DoctorService doctorService;

    @Autowired
    public PatientController(AppointmentService appointmentService, 
                             MedicalRecordService medicalRecordService, 
                             DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.doctorService = doctorService;
    }

    @GetMapping("/api/patient/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @PostMapping("/api/patient/appointment")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointment));
    }

    @GetMapping("/api/patient/appointments")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@RequestParam Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
    }

    @GetMapping("/api/patient/medicalrecords")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecords(@RequestParam Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByPatientId(patientId));
    }
}