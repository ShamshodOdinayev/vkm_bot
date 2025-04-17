package com.example.demo;

import jakarta.persistence.*;


@Entity
@Table(name = "profile")
public class ProfileEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;

}
