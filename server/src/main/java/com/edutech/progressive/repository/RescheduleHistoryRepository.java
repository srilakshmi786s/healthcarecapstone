package com.edutech.progressive.repository;

import com.edutech.progressive.entity.RescheduleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RescheduleHistoryRepository extends JpaRepository<RescheduleHistory, Integer> {
    List<RescheduleHistory> findByAppointment_AppointmentId(Integer appointmentId);
}
