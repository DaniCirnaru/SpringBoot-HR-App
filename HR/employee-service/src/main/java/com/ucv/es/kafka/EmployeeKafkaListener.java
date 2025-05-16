package com.ucv.es.kafka;

import com.ucv.es.dto.EmployeeCreatedEvent;
import com.ucv.es.entity.Employee;
import com.ucv.es.repository.EmployeeRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmployeeKafkaListener {

    private final EmployeeRepository employeeRepository;

    public EmployeeKafkaListener(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @KafkaListener(topics = "employees-topic", groupId = "employee-service")
    public void listen(EmployeeCreatedEvent event) {
        if (employeeRepository.findByUserId(Long.parseLong(event.getUserId())).isPresent()) {
            System.out.println("⚠️ Employee with userId " + event.getUserId() + " already exists.");
            return;
        }

        Employee employee = new Employee();
        employee.setUserId(Long.parseLong(event.getUserId()));
        employee.setName(event.getUsername());
        employee.setWorkEmail(event.getEmail());
        employee.setJobTitle(event.getJobTitle());
        employee.setDepartment(event.getDepartment());
        employee.setEmploymentType(event.getEmploymentType());
        employee.setHireDate(LocalDate.parse(event.getHireDate()));
        employee.setLocation(event.getLocation());

        employeeRepository.save(employee);
        System.out.println("✅ Employee created from Kafka event: " + event.getUsername());
    }
}
