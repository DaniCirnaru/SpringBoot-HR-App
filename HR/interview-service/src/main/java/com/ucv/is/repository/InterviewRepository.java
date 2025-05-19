package com.ucv.is.repository;

import com.ucv.is.entity.Interview;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByCandidateId(Long candidateId);

    @Query("SELECT i FROM Interview i JOIN i.employeeIds e WHERE e = :employeeId")
    List<Interview> findByEmployeeId(@Param("employeeId") Long employeeId);;
}
