package com.example.mcp_gateway.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mcp_server_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class McpServerConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serverName;

    @Column(nullable = false)
    private String endpointUrl;

    @Column(nullable = false)
    private String status; // UP, DOWN, UNKNOWN durumları için

    private LocalDateTime lastChecked; // En son kontrol edilme zamanı
    
}
