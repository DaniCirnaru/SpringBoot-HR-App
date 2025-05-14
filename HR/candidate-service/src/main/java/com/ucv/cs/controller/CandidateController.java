package com.ucv.cs.controller;

import com.ucv.cs.dto.CandidateDTO;
import com.ucv.cs.dto.CreateCandidateDTO;
import com.ucv.cs.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping
    public ResponseEntity<CandidateDTO> createCandidate(@RequestBody CreateCandidateDTO dto) {
        return ResponseEntity.ok(candidateService.createCandidate(dto));
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getCandidateById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateDTO> updateCandidate(@PathVariable Long id, @RequestBody CreateCandidateDTO dto) {
        return ResponseEntity.ok(candidateService.updateCandidate(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.ok("Candidate deleted successfully");
    }

    // ✅ New CV upload endpoint
    @PostMapping("/{id}/upload-cv")
    public ResponseEntity<String> uploadCv(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        candidateService.uploadCv(id, file);
        return ResponseEntity.ok("CV uploaded successfully");
    }
}
