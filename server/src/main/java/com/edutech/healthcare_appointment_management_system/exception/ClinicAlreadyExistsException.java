package com.edutech.healthcare_appointment_management_system.exception;

public class ClinicAlreadyExistsException extends RuntimeException {
    public ClinicAlreadyExistsException(String message) {
        super(message);
    }
}