package com.edutech.progressive.service;

import com.edutech.progressive.dto.MedicalRecordDTO;
import com.edutech.progressive.entity.Appointment;
import com.edutech.progressive.entity.MedicalRecord;
import com.edutech.progressive.repository.AppointmentRepository;
import com.edutech.progressive.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    public MedicalRecordDTO saveOrUpdateRecord(MedicalRecordDTO dto, Integer requestingDoctorId) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
            
        if (appointment.getClinic() == null || appointment.getClinic().getDoctor() == null) {
            throw new RuntimeException("Appointment not linked to a specific doctor.");
        }
        
        Integer appointedDoctorId = appointment.getClinic().getDoctor().getDoctorId();
        if(!appointedDoctorId.equals(requestingDoctorId)) {
            throw new RuntimeException("Unauthorized: You can only update records for your own appointments.");
        }
        
        MedicalRecord record = medicalRecordRepository.findByAppointment_AppointmentId(dto.getAppointmentId());
        if (record == null) {
            record = new MedicalRecord();
            record.setAppointment(appointment);
            record.setCreatedAt(new Date());
        }
        
        record.setNotes(dto.getNotes());
        record.setFollowUpInstructions(dto.getFollowUpInstructions());
        record.setAttachments(dto.getAttachments());
        
        return toDTO(medicalRecordRepository.save(record));
    }
    
    public List<MedicalRecordDTO> getPatientRecords(Integer patientId) {
        return medicalRecordRepository.findByAppointment_Patient_PatientId(patientId)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public MedicalRecordDTO getRecordByAppointment(Integer appointmentId) {
        MedicalRecord rec = medicalRecordRepository.findByAppointment_AppointmentId(appointmentId);
        return rec != null ? toDTO(rec) : null;
    }
    
    private MedicalRecordDTO toDTO(MedicalRecord entity) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setId(entity.getId());
        dto.setAppointmentId(entity.getAppointment().getAppointmentId());
        dto.setNotes(entity.getNotes());
        dto.setFollowUpInstructions(entity.getFollowUpInstructions());
        dto.setAttachments(entity.getAttachments());
        dto.setCreatedAt(entity.getCreatedAt());
        if(entity.getAppointment().getPatient() != null) {
            dto.setPatientId(entity.getAppointment().getPatient().getPatientId());
        }
        if(entity.getAppointment().getClinic() != null && entity.getAppointment().getClinic().getDoctor() != null) {
            dto.setDoctorId(entity.getAppointment().getClinic().getDoctor().getDoctorId());
            dto.setDoctorName(entity.getAppointment().getClinic().getDoctor().getFullName());
        }
        return dto;
    }
}
