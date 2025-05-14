package com.ucv.us.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateCreatedEvent {
    private String userId;
    private String email;
    private String username;
}

