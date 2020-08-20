package com.example.employee.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.employee.model.Employee;
import com.example.employee.model.Ticket;
import com.example.employee.repositories.EmployeeRepository;
import com.example.employee.repositories.TicketRepository;

@Component
public class DataLoader {
	private EmployeeRepository employeeRepository;
	private TicketRepository ticketRepository;

	@Autowired
	public DataLoader(EmployeeRepository employeeRepository, TicketRepository ticketRepository) {
		this.employeeRepository = employeeRepository;
		this.ticketRepository = ticketRepository;
		loadData();
	}

	private void loadData() {
		Employee emp1 = Employee.builder().employeeId(1L).name("Akhil").password("password").userName("Akhil")
				.authenticate(true).build();
		Employee emp2 = Employee.builder().employeeId(2L).name("Nikhil").password("password").userName("Nikhil")
				.authenticate(true).build();
		Employee emp3 = Employee.builder().employeeId(3L).name("Sukhil").password("password").userName("Sukhil")
				.authenticate(true).build();

		Ticket ticket1 = Ticket.builder().ticketId(11L).employee(emp1).description("system error").build();
		Ticket ticket2 = Ticket.builder().ticketId(22L).employee(emp2).description("invalid key").build();
		Ticket ticket3 = Ticket.builder().ticketId(33L).employee(emp3).description("os corrupted").build();

		employeeRepository.save(emp1);
		employeeRepository.save(emp2);
		employeeRepository.save(emp3);

		ticketRepository.save(ticket1);
		ticketRepository.save(ticket2);
		ticketRepository.save(ticket3);
	}

}
