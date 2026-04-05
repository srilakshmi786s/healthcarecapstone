package com.edutech.progressive.service.impl;

import com.edutech.progressive.entity.Appointment;
import com.edutech.progressive.repository.AppointmentRepository;
import com.edutech.progressive.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl  implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private com.edutech.progressive.repository.RescheduleHistoryRepository rescheduleHistoryRepository;

    @Autowired
    private com.edutech.progressive.service.NotificationService notificationService;

    @Autowired
    private com.edutech.progressive.repository.UserRepository userRepository;

    private void validateStatus(String status) {
        if (status != null && !status.equals("REQUESTED") && !status.equals("CONFIRMED") 
            && !status.equals("RESCHEDULED") && !status.equals("CANCELLED") 
            && !status.equals("COMPLETED")) {
            throw new RuntimeException("Invalid status: " + status);
        }
    }

    private void notifyUsers(Appointment appointment, String message, String type) {
        if (appointment.getPatient() != null) {
            com.edutech.progressive.entity.User uPatient = userRepository.findByPatientId(appointment.getPatient().getPatientId());
            if (uPatient != null) {
                notificationService.createNotification(uPatient.getUserId(), message, type);
            }
        }
        if (appointment.getClinic() != null && appointment.getClinic().getDoctor() != null) {
            com.edutech.progressive.entity.User uDoctor = userRepository.findByDoctorId(appointment.getClinic().getDoctor().getDoctorId());
            if (uDoctor != null) {
                notificationService.createNotification(uDoctor.getUserId(), message, type);
            }
        }
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public int createAppointment(Appointment appointment) {
        if(appointment.getStatus() == null) appointment.setStatus("REQUESTED");
        validateStatus(appointment.getStatus());
        int id = appointmentRepository.save(appointment).getAppointmentId();
        notifyUsers(appointment, "Appointment has been " + appointment.getStatus() + ".", "APPOINTMENT_CREATED");
        return id;
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        validateStatus(appointment.getStatus());
        appointmentRepository.save(appointment);
    }

    @Override
    public Appointment getAppointmentById(int appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));
    }

    @Override
    public List<Appointment> getAppointmentByClinic(int clinicId) {
        return appointmentRepository.findByClinic_ClinicId(clinicId);
    }

    @Override
    public List<Appointment> getAppointmentByPatient(int patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    @Override
    public List<Appointment> getAppointmentByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }

    @Override
    public void cancelAppointment(int appointmentId, String reason, String username) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus("CANCELLED");
        appointment.setCancellationReason(reason);
        appointmentRepository.save(appointment);
        notifyUsers(appointment, "Appointment was cancelled by " + username + ". Reason: " + reason, "APPOINTMENT_CANCELLED");
    }

    @Override
    public void rescheduleAppointment(int appointmentId, java.util.Date newDate, String username) {
        Appointment appointment = getAppointmentById(appointmentId);
        
        com.edutech.progressive.entity.RescheduleHistory history = new com.edutech.progressive.entity.RescheduleHistory();
        history.setAppointment(appointment);
        history.setPreviousTime(appointment.getAppointmentDate());
        history.setNewTime(newDate);
        history.setChangedBy(username);
        history.setTimestamp(new java.util.Date());
        
        rescheduleHistoryRepository.save(history);
        
        appointment.setAppointmentDate(newDate);
        appointment.setStatus("RESCHEDULED");
        appointmentRepository.save(appointment);
        notifyUsers(appointment, "Appointment was rescheduled to " + newDate + " by " + username + ".", "APPOINTMENT_RESCHEDULED");
    }
}