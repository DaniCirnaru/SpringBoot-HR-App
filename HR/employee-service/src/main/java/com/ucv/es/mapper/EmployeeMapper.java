package com.ucv.es.mapper;


import com.ucv.es.dto.EmployeeDTO;
import com.ucv.es.entity.Employee;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setUserId(dto.getUserId());
        employee.setName(dto.getName());
        employee.setWorkEmail(dto.getWorkEmail());
        employee.setJobTitle(dto.getJobTitle());
        employee.setDepartment(dto.getDepartment());
        employee.setEmploymentType(dto.getEmploymentType());
        employee.setHireDate(dto.getHireDate());
        employee.setSalary(dto.getSalary());
        employee.setLocation(dto.getLocation());
        employee.setSkills(dto.getSkills());

        return employee;
    }

    public static EmployeeDTO toDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getUserId(),
                employee.getName(),
                employee.getWorkEmail(),
                employee.getJobTitle(),
                employee.getDepartment(),
                employee.getEmploymentType(),
                employee.getHireDate(),
                employee.getSalary(),
                employee.getLocation(),
                employee.getSkills()
        );
    }
}
