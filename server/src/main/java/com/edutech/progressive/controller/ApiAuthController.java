package com.edutech.progressive.controller;

import com.edutech.progressive.dto.LoginRequest;
import com.edutech.progressive.dto.LoginResponse;
import com.edutech.progressive.dto.UserRegistrationDTO;
import com.edutech.progressive.entity.User;
import com.edutech.progressive.jwt.JwtUtil;
import com.edutech.progressive.service.impl.UserLoginServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiAuthController {

    @Autowired
    private UserLoginServiceImpl userLoginService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/patient/register")
    public ResponseEntity<?> registerPatient(@RequestBody UserRegistrationDTO dto) {
        dto.setRole("PATIENT");
        return registerUserInternal(dto);
    }

    @PostMapping("/doctors/register")
    public ResponseEntity<?> registerDoctor(@RequestBody UserRegistrationDTO dto) {
        dto.setRole("DOCTOR");
        return registerUserInternal(dto);
    }

    @PostMapping("/receptionist/register")
    public ResponseEntity<?> registerReceptionist(@RequestBody UserRegistrationDTO dto) {
        dto.setRole("RECEPTIONIST");
        return registerUserInternal(dto);
    }

    private ResponseEntity<?> registerUserInternal(UserRegistrationDTO dto) {
        try {
            userLoginService.registerUser(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = userLoginService.loadUserByUsername(loginRequest.getUsername());
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            User user = userLoginService.getUserByUsername(loginRequest.getUsername());
            String username = loginRequest.getUsername();
            Integer patientId = user.getPatient() != null ? user.getPatient().getPatientId() : null;
            Integer doctorId = user.getDoctor() != null ? user.getDoctor().getDoctorId() : null;
            return ResponseEntity.ok(new LoginResponse(token, user.getRole(), user.getUserId(), patientId, doctorId, username));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new LoginResponse("Invalid username or password", null, null, null, null, null));
        }
    }
}
