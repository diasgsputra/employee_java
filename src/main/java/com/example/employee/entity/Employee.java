package com.example.employee.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "employees")
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String email;
  private String department;
}
