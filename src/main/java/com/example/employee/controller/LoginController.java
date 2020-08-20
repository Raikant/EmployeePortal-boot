package com.example.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.model.Employee;
import com.example.employee.service.LoginService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("employee")
@Slf4j
public class LoginController {

	@Autowired
	private LoginService loginService;

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> login(@RequestBody Employee employee) {
		log.info(employee.getUserName() + "," + employee.getPassword());
		return loginService.employeeLogin(employee);
	}
}
