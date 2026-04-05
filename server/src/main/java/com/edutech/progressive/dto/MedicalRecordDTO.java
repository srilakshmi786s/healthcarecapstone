package com.edutech.progressive.dto;

import java.util.Date;

public class MedicalRecordDTO {
    private Integer id;
    private Integer appointmentId;
    private String notes;
    private String followUpInstructions;
    private String attachments;
    private Date createdAt;
    
    // extra info for GETs
    private Integer patientId;
    private Integer doctorId;
    private String doctorName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFollowUpInstructions() {
        return followUpInstructions;
    }

    public void setFollowUpInstructions(String followUpInstructions) {
        this.followUpInstructions = followUpInstructions;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
