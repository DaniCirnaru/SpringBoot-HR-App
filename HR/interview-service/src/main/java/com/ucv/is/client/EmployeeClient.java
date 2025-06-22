package com.ucv.is.client;

import com.ucv.is.dto.EmployeeInfoDTO;
import com.ucv.is.security.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", url = "${employee.service.url}", configuration = FeignConfig.class)
public interface EmployeeClient {

    @GetMapping("/api/employees/{id}/info")
    EmployeeInfoDTO getEmployeeInfo(@PathVariable Long id);
    @GetMapping("/api/employees/info-by-email/{email}")
    EmployeeInfoDTO getEmployeeInfoByEmail(@PathVariable String email);

}
