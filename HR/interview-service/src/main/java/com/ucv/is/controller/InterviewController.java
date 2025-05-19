package com.ucv.is.controller;

import com.ucv.is.dto.InterviewDTO;
import com.ucv.is.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService service;

    public InterviewController(InterviewService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InterviewDTO> create(@RequestBody InterviewDTO dto) {
        return ResponseEntity.ok(service.scheduleInterview(dto));
    }

    @GetMapping("/candidate/{id}")
    public ResponseEntity<List<InterviewDTO>> getByCandidate(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByCandidate(id));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<InterviewDTO>> getByEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByEmployee(id));
    }
}
