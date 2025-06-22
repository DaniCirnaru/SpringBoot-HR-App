package com.ucv.is.controller;

import com.ucv.is.dto.InterviewDTO;
import com.ucv.is.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @PatchMapping("/{id}/feedback")
    public ResponseEntity<Void> updateFeedback(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String feedback = body.get("feedback");
        service.updateFeedback(id, feedback);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/candidate/{id}")
    public ResponseEntity<List<InterviewDTO>> getByCandidate(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByCandidate(id));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<InterviewDTO>> getByEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByEmployee(id));
    }
    @GetMapping
    public ResponseEntity<List<InterviewDTO>> getAll() {
        return ResponseEntity.ok(service.getAllInterviews());
    }
    @GetMapping("/assigned")
    public ResponseEntity<List<InterviewDTO>> getAssignedInterviews(Authentication authentication) {
        String email = authentication.getName(); // comes from JWT
        List<InterviewDTO> interviews = service.getByEmployeeEmail(email);
        return ResponseEntity.ok(interviews);
    }

    @GetMapping("/my")
    public ResponseEntity<List<InterviewDTO>> getMyInterviews(Authentication authentication) {
        String email = authentication.getName(); // email from JWT
        List<InterviewDTO> interviews = service.getByCandidateEmail(email);
        return ResponseEntity.ok(interviews);
    }

}
