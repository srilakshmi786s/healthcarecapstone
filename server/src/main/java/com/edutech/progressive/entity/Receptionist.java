package com.edutech.progressive.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "receptionists")
@PrimaryKeyJoinColumn(name = "user_id")
public class Receptionist extends User {

    public Receptionist() {
    }
}
