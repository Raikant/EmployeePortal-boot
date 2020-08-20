package com.example.employee.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employee.model.Employee;
import com.example.employee.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findAllByEmployee(Employee employee);

	Ticket findByEmployeeAndTicketId(Employee employee, Long ticketId);

}
