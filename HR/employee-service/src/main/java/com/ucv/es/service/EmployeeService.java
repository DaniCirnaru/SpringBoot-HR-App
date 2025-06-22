package com.ucv.es.service;

import com.ucv.es.dto.EmployeeDTO;
import com.ucv.es.dto.EmployeeInfoDTO;
import com.ucv.es.entity.Employee;
import com.ucv.es.exception.EmployeeNotFoundException;
import com.ucv.es.mapper.EmployeeMapper;
import com.ucv.es.repository.EmployeeRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDTO> getAll() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::toDTO)
                .toList();
    }

    public Optional<EmployeeDTO> getByUserId(Long userId) {
        return employeeRepository.findByUserId(userId)
                .map(EmployeeMapper::toDTO);
    }
    public EmployeeInfoDTO getEmployeeInfoByEmail(String email) {return employeeRepository.findByWorkEmail(email)
            .map(EmployeeMapper::toInfoDTO)
            .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));

    }


    public EmployeeInfoDTO getEmployeeInfoById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));


        return new EmployeeInfoDTO(
                employee.getId(),
                employee.getName(),
                employee.getJobTitle(),
                employee.getDepartment(),
                employee.getSkills()
        );
    }
    public List<EmployeeInfoDTO> getAllInfo() {
        return employeeRepository.findAll()
                .stream()
                .map(emp -> new EmployeeInfoDTO(
                        emp.getId(),
                        emp.getName(),
                        emp.getJobTitle(),
                        emp.getDepartment(),
                        emp.getSkills()
                ))
                .toList();
    }


    public boolean existsByUserId(Long userId) {
        return employeeRepository.findByUserId(userId).isPresent();
    }

    public EmployeeDTO save(EmployeeDTO dto) {
        // Prevent duplicates
        if (existsByUserId(dto.getUserId())) {
            throw new IllegalStateException("Employee with userId " + dto.getUserId() + " already exists.");
        }

        Employee employee = EmployeeMapper.toEntity(dto);
        Employee saved = employeeRepository.save(employee);
        return EmployeeMapper.toDTO(saved);
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
