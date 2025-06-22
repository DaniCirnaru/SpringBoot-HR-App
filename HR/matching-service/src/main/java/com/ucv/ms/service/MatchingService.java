package com.ucv.ms.service;

import com.ucv.ms.client.CandidateClient;
import com.ucv.ms.client.EmployeeClient;
import com.ucv.ms.dto.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private final CandidateClient candidateClient;
    private final EmployeeClient employeeClient;

    public MatchingService(CandidateClient candidateClient, EmployeeClient employeeClient) {
        this.candidateClient = candidateClient;
        this.employeeClient = employeeClient;
    }

    public List<MatchResultDTO> findBestMatches(Long candidateId) {
        CandidateInfoDTO candidate = candidateClient.getCandidateInfo(candidateId);
        List<EmployeeInfoDTO> employees = employeeClient.getAllEmployees();

        return employees.stream()
                .map(emp -> {
                    double score = computeMatchScore(candidate, emp);
                    MatchResultDTO result = new MatchResultDTO();
                    result.setEmployeeId(emp.getId());
                    result.setName(emp.getName());
                    result.setJobTitle(emp.getJobTitle());
                    result.setScore(score);
                    return result;
                })
                .filter(match -> match.getScore() >= 0) // optional: only return relevant matches
                .sorted(Comparator.comparingDouble(MatchResultDTO::getScore).reversed())
                .collect(Collectors.toList());
    }

    private double computeMatchScore(CandidateInfoDTO candidate, EmployeeInfoDTO employee) {
        double skillMatch = jaccardSimilarity(candidate.getRecognizedSkills(), employee.getSkills());

        double titleMatch = candidate.getPosition().equalsIgnoreCase(employee.getJobTitle()) ? 0.2 : 0.0;

        return skillMatch + titleMatch;
    }

    private double jaccardSimilarity(List<String> a, List<String> b) {
        if (a == null || b == null || a.isEmpty() || b.isEmpty()) return 0.0;

        Set<String> setA = a.stream().map(String::toLowerCase).collect(Collectors.toSet());
        Set<String> setB = b.stream().map(String::toLowerCase).collect(Collectors.toSet());

        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        return (double) intersection.size() / union.size();
    }

    public List<MatchResultDTO> findTopMatches(Long candidateId, int count) {
        List<MatchResultDTO> allMatches = findBestMatches(candidateId);

        return allMatches.stream()
                .sorted(Comparator.comparingDouble(MatchResultDTO::getScore).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Map<String, List<MatchResultDTO>> getMatchesGroupedByDepartment(Long candidateId) {
        CandidateInfoDTO candidate = candidateClient.getCandidateInfo(candidateId);
        List<EmployeeInfoDTO> employees = employeeClient.getAllEmployees();

        return employees.stream()
                .map(emp -> {
                    double score = computeMatchScore(candidate, emp);
                    MatchResultDTO match = new MatchResultDTO();
                    match.setEmployeeId(emp.getId());
                    match.setName(emp.getName());
                    match.setJobTitle(emp.getJobTitle());
                    match.setScore(score);
                    return new AbstractMap.SimpleEntry<>(emp.getDepartment(), match);
                })
                .filter(entry -> entry.getValue().getScore() > 0)
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }

}
