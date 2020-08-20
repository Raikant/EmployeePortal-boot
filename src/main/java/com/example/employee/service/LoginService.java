package com.example.employee.service;

import org.springframework.http.ResponseEntity;

import com.example.employee.model.Employee;

public interface LoginService {

	ResponseEntity<Employee> employeeLogin(Employee employee);

}
