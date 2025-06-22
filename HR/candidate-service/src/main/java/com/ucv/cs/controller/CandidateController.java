package com.ucv.cs.controller;

import com.ucv.cs.dto.CandidateDTO;
import com.ucv.cs.dto.CandidateInfoDTO;
import com.ucv.cs.dto.CreateCandidateDTO;
import com.ucv.cs.dto.UpdateSkillsDTO;
import com.ucv.cs.entity.Candidate;
import com.ucv.cs.mapper.CandidateMapper;
import com.ucv.cs.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private CandidateMapper candidateMapper;

    @GetMapping("/me")
    public ResponseEntity<CandidateDTO> getCurrentCandidate(Authentication authentication) {
        String email = authentication.getName(); // email from JWT `sub`
        Candidate candidate = candidateService.getByEmail(email);
        return ResponseEntity.ok(candidateMapper.toDto(candidate));
    }

    @PostMapping
    public ResponseEntity<CandidateDTO> createCandidate(@RequestBody CreateCandidateDTO dto) {
        return ResponseEntity.ok(candidateService.createCandidate(dto));
    }

    @GetMapping("/info-by-user/{userId}")
    public ResponseEntity<CandidateInfoDTO> getCandidateInfoByUserId(@PathVariable Long userId) {
        CandidateInfoDTO dto = candidateService.getCandidateInfoByUserId(userId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/upload-cv-by-user/{userId}")
    public ResponseEntity<String> uploadCvByUserId(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        candidateService.uploadCvByUserId(userId, file);
        return ResponseEntity.ok("CV uploaded successfully");
    }

    @PatchMapping("/{id}/skills")
    public ResponseEntity<CandidateDTO> updateSkills(
            @PathVariable Long id,
            @RequestBody UpdateSkillsDTO dto) {
        Candidate updated = candidateService.updateSkills(id, dto.getRecognizedSkills());
        return ResponseEntity.ok(candidateMapper.toDto(updated));
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getCandidateById(id));
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<CandidateInfoDTO> getCandidateInfo(@PathVariable Long id) {
        CandidateInfoDTO dto = candidateService.getCandidateInfoById(id);
        return ResponseEntity.ok(dto);
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
    @GetMapping("/info-by-email/{email}")
    public ResponseEntity<CandidateInfoDTO> getCandidateInfoByEmail(@PathVariable String email) {
        CandidateInfoDTO dto = candidateService.getCandidateInfoByEmail(email);
        return ResponseEntity.ok(dto);
    }

}
