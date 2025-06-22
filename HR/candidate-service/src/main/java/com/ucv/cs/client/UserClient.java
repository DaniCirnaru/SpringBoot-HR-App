package com.ucv.cs.client;

import com.ucv.cs.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/users")
public interface UserClient {

    @GetMapping("/email/{email}")
    UserDTO getUserByEmail(@PathVariable String email);
}
