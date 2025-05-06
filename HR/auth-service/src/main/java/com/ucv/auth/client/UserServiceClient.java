package com.ucv.auth.client;

import com.ucv.auth.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserServiceClient {

    @GetMapping("/email/{email}")
    UserDTO getUserByEmail(@PathVariable String email);
}
