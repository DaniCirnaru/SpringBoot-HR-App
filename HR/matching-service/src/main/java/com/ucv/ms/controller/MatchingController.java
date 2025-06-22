package com.ucv.ms.controller;

import com.ucv.ms.dto.MatchResultDTO;
import com.ucv.ms.service.MatchingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/candidate/{id}")
    public List<MatchResultDTO> getMatches(@PathVariable Long id) {
        return matchingService.findBestMatches(id);
    }
    @GetMapping("/candidate/{id}/top/{count}")
    public List<MatchResultDTO> getTopMatches(@PathVariable Long id, @PathVariable int count) {
        return matchingService.findTopMatches(id, count);
    }

    @GetMapping("/candidate/{id}/grouped")
    public Map<String, List<MatchResultDTO>> getGroupedMatches(@PathVariable Long id) {
        return matchingService.getMatchesGroupedByDepartment(id);
    }

}
