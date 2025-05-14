package com.ucv.cs.util;


import java.util.*;
import java.util.stream.Collectors;

public class SkillExtractor {

    // Define your known skill dictionary (ideally load this from DB or file in real apps)
    private static final Set<String> KNOWN_SKILLS = Set.of(
            "java", "spring", "spring boot", "hibernate", "sql",
            "python", "javascript", "react", "angular", "docker",
            "kubernetes", "git", "linux", "html", "css",
            "maven", "gradle", "aws", "azure", "jenkins"
    );

    /**
     * Extract known skills from plain CV text.
     *
     * @param text Raw text extracted from a CV
     * @return List of matched skills
     */
    public static List<String> extractSkills(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String lowercaseText = text.toLowerCase();

        return KNOWN_SKILLS.stream()
                .filter(lowercaseText::contains)
                .collect(Collectors.toList());
    }
}
