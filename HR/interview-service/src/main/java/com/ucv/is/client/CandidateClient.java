package com.ucv.is.client;

import com.ucv.is.dto.CandidateInfoDTO;
import com.ucv.is.security.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "candidate-service",
        url = "${candidate.service.url}",
        configuration = FeignConfig.class
)
public interface CandidateClient {

    @GetMapping("/api/candidates/{id}/info")
    CandidateInfoDTO getCandidateInfo(@PathVariable Long id);

    @GetMapping("/api/candidates/info-by-email/{email}")
    CandidateInfoDTO getCandidateInfoByEmail(@PathVariable String email);

}

