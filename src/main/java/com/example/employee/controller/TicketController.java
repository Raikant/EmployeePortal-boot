package com.example.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.model.Ticket;
import com.example.employee.service.TicketService;

@RestController
@RequestMapping("ticket")

@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> list() {
		return ticketService.list();
	}

	@PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Ticket> add(@RequestBody Ticket ticket) {
		return ticketService.add(ticket);
	}

	@PostMapping(value = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpStatus> delete(@RequestBody Ticket ticket) {
		return ticketService.delete(ticket);
	}

	@PutMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Ticket> update(@RequestBody Ticket ticket) {
		return ticketService.update(ticket);
	}
}
