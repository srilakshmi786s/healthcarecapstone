package com.edutech.progressive.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reschedule_history")
public class RescheduleHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(name = "previous_time", nullable = false)
    private Date previousTime;

    @Column(name = "new_time", nullable = false)
    private Date newTime;

    @Column(name = "changed_by", nullable = false)
    private String changedBy;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Date getPreviousTime() {
        return previousTime;
    }

    public void setPreviousTime(Date previousTime) {
        this.previousTime = previousTime;
    }

    public Date getNewTime() {
        return newTime;
    }

    public void setNewTime(Date newTime) {
        this.newTime = newTime;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
