package com.edutech.progressive.controller;

import com.edutech.progressive.dto.DoctorAvailabilityDTO;
import com.edutech.progressive.service.DoctorAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiAvailabilityController {

    @Autowired
    private DoctorAvailabilityService availabilityService;

    @PostMapping("/doctor/availability")
    public ResponseEntity<?> addDoctorAvailability(@RequestBody DoctorAvailabilityDTO dto) {
        try {
            DoctorAvailabilityDTO saved = availabilityService.addAvailability(dto);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/patient/availability")
    public ResponseEntity<?> getDoctorAvailability(
            @RequestParam("doctorId") Integer doctorId,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        try {
            List<DoctorAvailabilityDTO> availabilities = availabilityService.getAvailabilityByDoctorAndDate(doctorId, date);
            return new ResponseEntity<>(availabilities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
