package com.example.employee.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.employee.model.Employee;
import com.example.employee.repositories.EmployeeRepository;
import com.example.employee.service.EmployeeService;
import com.example.employee.util.JwtUtility;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class EmplyeeServiceImpl implements EmployeeService {

	private String authtoken = "authtoken";

	@Autowired
	private HttpServletRequest httpRequest;
	@Autowired
	private JwtUtility jwtUtility;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public ResponseEntity<HttpStatus> addEmployee(Employee employee) {

		String authToken = httpRequest.getHeader(authtoken);
		if (null != authToken) {
			try {
				employee = jwtUtility.parseJWT(authToken);
				Employee emp = employeeRepository.save(employee);
				if (emp.getEmployeeId() != null)
					return new ResponseEntity<>(HttpStatus.OK);
			} catch (ExpiredJwtException ex) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			} catch (UnsupportedJwtException | MalformedJwtException | SignatureException
					| IllegalArgumentException ex) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
