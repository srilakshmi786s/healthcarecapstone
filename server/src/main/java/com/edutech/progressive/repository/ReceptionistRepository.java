package com.edutech.progressive.repository;

import com.edutech.progressive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistRepository extends JpaRepository<User, Long> {
}
