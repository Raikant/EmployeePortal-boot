package com.example.employee.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.employee.model.Ticket;

public interface TicketService {
	public ResponseEntity<List<Ticket>> list();

	ResponseEntity<Ticket> add(Ticket ticket);

	public ResponseEntity<HttpStatus> delete(Ticket ticket);

	public ResponseEntity<Ticket> update(Ticket ticket);
}
