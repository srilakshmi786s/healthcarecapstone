package com.edutech.progressive.controller;

import com.edutech.progressive.entity.Appointment;
import com.edutech.progressive.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiAppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/doctor/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }
    
    @GetMapping("/patient/appointments")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@RequestParam("patientId") Integer patientId) {
        return new ResponseEntity<>(appointmentService.getAppointmentByPatient(patientId), HttpStatus.OK);
    }
    
    @PostMapping("/patient/appointment")
    public ResponseEntity<?> createPatientAppointment(@RequestBody Appointment appointment) {
        appointment.setStatus("REQUESTED");
        int id = appointmentService.createAppointment(appointment);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
    
    @PutMapping("/patient/appointment-cancel/{appointmentId}")
    public ResponseEntity<?> cancelPatientAppointment(@PathVariable Integer appointmentId, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        appointmentService.cancelAppointment(appointmentId, reason, "PATIENT");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/receptionist/appointments")
    public ResponseEntity<List<Appointment>> getReceptionistAppointments() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }

    @PostMapping("/receptionist/appointment")
    public ResponseEntity<?> createReceptionistAppointment(@RequestBody Appointment appointment) {
        appointment.setStatus("CONFIRMED");
        int id = appointmentService.createAppointment(appointment);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("/receptionist/appointment-reschedule/{appointmentId}")
    public ResponseEntity<?> rescheduleAppointment(
            @PathVariable Integer appointmentId, 
            @RequestBody Map<String, Object> body) {
        try {
            Long timeStr = (Long) body.get("newTime");
            Date newDate = new Date(timeStr);
            appointmentService.rescheduleAppointment(appointmentId, newDate, "RECEPTIONIST");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/receptionist/appointment-cancel/{appointmentId}")
    public ResponseEntity<?> cancelReceptionistAppointment(@PathVariable Integer appointmentId, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        appointmentService.cancelAppointment(appointmentId, reason, "RECEPTIONIST");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
