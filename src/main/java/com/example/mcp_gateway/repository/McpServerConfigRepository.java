package com.example.mcp_gateway.repository;

import com.example.mcp_gateway.model.McpServerConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface McpServerConfigRepository extends JpaRepository<McpServerConfig, Long> {
    
    // Sunucu ismine göre veritabanında zaten kayıt var mı yok mu kontrol eden özel metot
    boolean existsByServerName(String serverName);
}