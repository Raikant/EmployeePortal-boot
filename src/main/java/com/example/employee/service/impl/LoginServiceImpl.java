package com.example.employee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.employee.model.Employee;
import com.example.employee.repositories.EmployeeRepository;
import com.example.employee.service.LoginService;
import com.example.employee.util.JwtUtility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private JwtUtility jWTUtility;

	@Override
	public ResponseEntity<Employee> employeeLogin(Employee employee) {
		Employee authenticatedUser = null;
		try {
			/**
			 * below line used to generate a new encrypted password for given string
			 */
//			log.info("encryptedMsg:{}", jWTUtility.encryptMessage("password"));
//			String password = jWTUtility.decryptMessage(employee.getPassword());
//			log.info("decrypted password={}", password);
			authenticatedUser = authenticatUserWithDB(employee.getUserName(), employee.getPassword());
			if (authenticatedUser != null) {
				employee.setAuthenticate(Boolean.TRUE);
				String authToken = jWTUtility.getJWT(authenticatedUser);
				employee.setAuthToken(authToken);
			}
		} catch (Exception e) {
			authenticatedUser.setAuthenticate(Boolean.FALSE);
			return new ResponseEntity<Employee>(employee, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}

	private Employee authenticatUserWithDB(String userName, String password) {
		return employeeRepository.findByUserNameAndPassword(userName, password);
	}
}
