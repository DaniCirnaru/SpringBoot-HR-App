package com.ucv.cs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "candidates")
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;


    /**
     * Reference to the user ID from the User Service.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Position the candidate is applying for.
     */
    @Column(nullable = false)
    private String position;

    /**
     * Candidate's CV file stored as a binary blob.
     */
    @Lob
    @Column(name = "cv_data", columnDefinition = "BLOB")
    private byte[] cvData;


}
