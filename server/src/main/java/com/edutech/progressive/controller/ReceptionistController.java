package com.edutech.progressive.controller;

import com.edutech.progressive.dto.TimeDto;
import com.edutech.progressive.entity.Appointment;
import com.edutech.progressive.entity.MedicalRecord;
import com.edutech.progressive.service.AppointmentService;
import com.edutech.progressive.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReceptionistController {

    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;

    @Autowired
    public ReceptionistController(AppointmentService appointmentService, MedicalRecordService medicalRecordService) {
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/api/receptionist/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PostMapping("/api/receptionist/appointment")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointment));
    }

    @GetMapping("/api/receptionist/patient/appointments")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@RequestParam Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
    }

    @GetMapping("/api/receptionist/patient/medicalrecords")
    public ResponseEntity<List<MedicalRecord>> getPatientMedicalRecords(@RequestParam Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByPatientId(patientId));
    }

    @PutMapping("/api/receptionist/appointment-reschedule/{appointmentId}")
    public ResponseEntity<Appointment> rescheduleAppointment(@PathVariable Long appointmentId, @RequestBody TimeDto timeDto) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setAppointmentTime(timeDto.getTime());
            appointment.setStatus("RESCHEDULED");
            return ResponseEntity.ok(appointmentService.createAppointment(appointment));
        }
        return ResponseEntity.notFound().build();
    }
}
