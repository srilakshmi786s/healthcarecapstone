
package com.edutech.healthcare_appointment_management_system.repository;

import com.edutech.healthcare_appointment_management_system.entity.Clinic;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Integer> {

    Clinic findByClinicId(int clinicId);

    List<Clinic> findAllByLocation(String location);

    Clinic findByClinicName(String clinicName);

    @Query("SELECT c FROM Clinic c WHERE c.doctor.doctorId = :doctorId")
    List<Clinic> findAllByDoctorId(int doctorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Clinic c WHERE c.doctor.doctorId = :doctorId")
    void deleteByDoctorId(int doctorId);


}
