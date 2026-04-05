package com.edutech.healthcare_appointment_management_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtRequestFilter jwtRequestFilter,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/patient/register", "/api/doctors/register", "/api/receptionist/register", "/api/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/doctor/availability").hasAuthority("DOCTOR")
                .antMatchers(HttpMethod.POST, "/api/patient/appointment").hasAuthority("PATIENT")
                .antMatchers(HttpMethod.POST, "/api/receptionist/appointment").hasAuthority("RECEPTIONIST")
                .antMatchers(HttpMethod.GET, "/api/patient/doctors", "/api/patient/appointments", "/api/patient/medicalrecords").hasAuthority("PATIENT")
                .antMatchers(HttpMethod.GET, "/api/doctor/appointments").hasAuthority("DOCTOR")
                .antMatchers(HttpMethod.GET, "/api/receptionist/appointments").hasAuthority("RECEPTIONIST")
                .antMatchers(HttpMethod.PUT, "/api/receptionist/appointment-reschedule/{appointmentId}").hasAuthority("RECEPTIONIST")
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}