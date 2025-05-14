package com.ucv.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateCreatedEvent {
    private String userId;
    private String email;
    private String username;

}
