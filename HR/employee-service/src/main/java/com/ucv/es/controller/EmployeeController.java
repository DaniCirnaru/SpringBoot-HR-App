package com.ucv.es.controller;

import com.ucv.es.dto.EmployeeDTO;
import com.ucv.es.dto.EmployeeInfoDTO;
import com.ucv.es.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAll();
        return ResponseEntity.ok(employees);
    }
    @GetMapping("/{id}/info")
    public ResponseEntity<EmployeeInfoDTO> getEmployeeInfo(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeInfoById(id));
    }


    @GetMapping("/by-user/{userId}")
    public ResponseEntity<EmployeeDTO> getByUserId(@PathVariable Long userId) {
        return employeeService.getByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO saved = employeeService.save(employeeDTO);
            return ResponseEntity.ok(saved);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
