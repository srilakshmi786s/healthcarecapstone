package com.edutech.progressive.service;

import com.edutech.progressive.dto.DoctorAvailabilityDTO;
import com.edutech.progressive.entity.Doctor;
import com.edutech.progressive.entity.DoctorAvailability;
import com.edutech.progressive.repository.DoctorAvailabilityRepository;
import com.edutech.progressive.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorAvailabilityService {

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public DoctorAvailabilityDTO addAvailability(DoctorAvailabilityDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        DoctorAvailability availability = new DoctorAvailability();
        availability.setDoctor(doctor);
        availability.setAvailableDate(dto.getAvailableDate());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        availability.setStatus(dto.getStatus() == null ? "AVAILABLE" : dto.getStatus());

        DoctorAvailability saved = availabilityRepository.save(availability);
        return toDTO(saved);
    }

    public List<DoctorAvailabilityDTO> getAvailabilityByDoctorAndDate(Integer doctorId, Date date) {
        List<DoctorAvailability> list;
        if (date != null) {
            list = availabilityRepository.findByDoctor_DoctorIdAndAvailableDate(doctorId, date);
        } else {
            list = availabilityRepository.findByDoctor_DoctorId(doctorId);
        }
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private DoctorAvailabilityDTO toDTO(DoctorAvailability entity) {
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        dto.setId(entity.getId());
        dto.setDoctorId(entity.getDoctor().getDoctorId());
        dto.setAvailableDate(entity.getAvailableDate());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
