package com.example.employee.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.employee.model.Employee;

public interface EmployeeService {

	ResponseEntity<HttpStatus> addEmployee(Employee employee);

}
