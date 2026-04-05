package com.edutech.progressive.controller;

import com.edutech.progressive.config.JwtUtil;
import com.edutech.progressive.dto.LoginRequest;
import com.edutech.progressive.dto.LoginResponse;
import com.edutech.progressive.entity.Doctor;
import com.edutech.progressive.entity.Patient;
import com.edutech.progressive.entity.Receptionist;
import com.edutech.progressive.entity.User;
import com.edutech.progressive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterAndLoginController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public RegisterAndLoginController(UserService userService, 
                                      AuthenticationManager authenticationManager, 
                                      JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/patient/register")
    public ResponseEntity<User> registerPatient(@RequestBody Patient patient) {
        patient.setRole("PATIENT");
        return ResponseEntity.ok(userService.registerUser(patient));
    }

    @PostMapping("/api/doctors/register")
    public ResponseEntity<User> registerDoctor(@RequestBody Doctor doctor) {
        doctor.setRole("DOCTOR");
        return ResponseEntity.ok(userService.registerUser(doctor));
    }

    @PostMapping("/api/receptionist/register")
    public ResponseEntity<User> registerReceptionist(@RequestBody Receptionist receptionist) {
        receptionist.setRole("RECEPTIONIST");
        return ResponseEntity.ok(userService.registerUser(receptionist));
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        User user = userService.getUserByUsername(loginRequest.getUsername());

        return ResponseEntity.ok(new LoginResponse(user.getId(), jwt, user.getUsername(), user.getEmail(), user.getRole()));
    }
}
