package com.example.mcp_gateway.repository;

import com.example.mcp_gateway.model.AgenticWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenticWorkflowRepository extends JpaRepository<AgenticWorkflow, Long> {
    // Kurumsal kayıt (save) ve listeleme (findAll) metotları JpaRepository ile otomatik hazır gelir.
}