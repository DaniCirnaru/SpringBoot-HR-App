package com.ucv.ms.client;

import com.ucv.ms.dto.EmployeeInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "employee-service", url = "${employee.service.url}")
public interface EmployeeClient {
    @GetMapping("/api/employees/info")
    List<EmployeeInfoDTO> getAllEmployees();
}

