package com.edutech.healthcare_appointment_management_system.controller;

import com.edutech.healthcare_appointment_management_system.entity.Appointment;
import com.edutech.healthcare_appointment_management_system.entity.Doctor;
import com.edutech.healthcare_appointment_management_system.service.AppointmentService;
import com.edutech.healthcare_appointment_management_system.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DoctorController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(AppointmentService appointmentService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    @PostMapping("/api/doctor/availability")
    public ResponseEntity<Doctor> updateAvailability(@RequestParam Long doctorId, @RequestParam String availability) {
        Doctor doctor = doctorService.updateAvailability(doctorId, availability);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/api/doctor/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@RequestParam Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorId));
    }
}