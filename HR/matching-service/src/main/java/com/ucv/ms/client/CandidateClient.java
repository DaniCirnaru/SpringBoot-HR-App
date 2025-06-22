package com.ucv.ms.client;

import com.ucv.ms.dto.CandidateInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "candidate-service", url = "${candidate.service.url}")
public interface CandidateClient {
    @GetMapping("/api/candidates/{id}/info")
    CandidateInfoDTO getCandidateInfo(@PathVariable Long id);
}
