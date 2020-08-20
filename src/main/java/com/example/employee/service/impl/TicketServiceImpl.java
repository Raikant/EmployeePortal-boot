package com.example.employee.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.employee.model.Employee;
import com.example.employee.model.Ticket;
import com.example.employee.repositories.TicketRepository;
import com.example.employee.service.TicketService;
import com.example.employee.util.JwtUtility;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TicketServiceImpl implements TicketService {
	private String authtoken = "authtoken";

	@Autowired
	private HttpServletRequest httpRequest;
	@Autowired
	private JwtUtility jwtUtility;
	@Autowired
	private TicketRepository ticketRepository;

	@Override
	public ResponseEntity<List<Ticket>> list() {
		Employee employee = null;
		String authToken = httpRequest.getHeader(authtoken);
		if (null != authToken) {
			try {
				employee = jwtUtility.parseJWT(authToken);
				List<Ticket> ticketList = ticketRepository.findAllByEmployee(employee);
				return new ResponseEntity<>(ticketList, HttpStatus.OK);
			} catch (ExpiredJwtException ex) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			} catch (UnsupportedJwtException | MalformedJwtException | SignatureException
					| IllegalArgumentException ex) {
				ex.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Ticket validateRequest(Employee employee, Ticket ticket) {
		Ticket ticketFound = ticketRepository.findByEmployeeAndTicketId(employee, ticket.getTicketId());
		if (ticketFound == null) {
			return null;
		}
		return ticketFound;
	}

	@Override
	public ResponseEntity<HttpStatus> delete(Ticket ticket) {
		Employee employee = null;
		String authToken = httpRequest.getHeader(authtoken);
		if (null != authToken) {
			try {
				Ticket ticketFound = null;
				employee = jwtUtility.parseJWT(authToken);
				if (employee != null && ticket != null) {
					ticketFound = ticketRepository.findByEmployeeAndTicketId(employee, ticket.getTicketId());
				}
				if (ticketFound == null) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				ticketRepository.delete(ticket);
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (ExpiredJwtException ex) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			} catch (UnsupportedJwtException | MalformedJwtException | SignatureException
					| IllegalArgumentException ex) {
				ex.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Ticket> add(Ticket ticket) {
		Employee employee = null;
		String authToken = httpRequest.getHeader(authtoken);
		if (null != authToken && ticket != null) {
			try {
				employee = jwtUtility.parseJWT(authToken);
				if (employee != null) {
					ticket.setEmployee(employee);
					ticketRepository.save(ticket);
					return new ResponseEntity<>(ticket, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} catch (ExpiredJwtException ex) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			} catch (UnsupportedJwtException | MalformedJwtException | SignatureException
					| IllegalArgumentException ex) {
				ex.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Ticket> update(Ticket ticket) {

		Employee employee = null;
		String authToken = httpRequest.getHeader(authtoken);
		if (null != authToken && ticket != null) {
			try {
				employee = jwtUtility.parseJWT(authToken);
				Ticket existingTicket = validateRequest(employee, ticket);
				if (employee != null && existingTicket != null) {
					ticket.setCreatedAt(existingTicket.getCreatedAt());
					ticket.setEmployee(employee);
					ticketRepository.save(ticket);
					return new ResponseEntity<>(ticket,HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} catch (ExpiredJwtException ex) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			} catch (UnsupportedJwtException | MalformedJwtException | SignatureException
					| IllegalArgumentException ex) {
				ex.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
