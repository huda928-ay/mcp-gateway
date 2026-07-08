package com.example.mcp_gateway.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class NexusMockTool {

    @Tool("Nexus artifact deposundaki (repository) son yüklenen paketleri, imajları ve versiyonları listeler.")
    public String getLatestArtifacts(String repositoryName) {
        System.out.println("[MCP GATEWAY LOG] NexusMockTool tetiklendi: getLatestArtifacts() -> " + repositoryName);
        
        // 🛠️ Savunma Odaklı Giriş Kontrolü (Validation)
        if (repositoryName == null || repositoryName.trim().isEmpty()) {
            return "Hata: Sorgulanacak depo (repository) adı anlaşılamadı. Lütfen 'maven', 'npm' veya 'docker' depolarından birini belirtin.";
        }

        String cleanedRepo = repositoryName.trim().toLowerCase();

        if (cleanedRepo.contains("maven") || cleanedRepo.contains("release")) {
            return "Nexus Raporu [maven-releases]:\n" +
                   "- com.example:mcp-gateway:jar:1.0.2 (Yüklendi: 2 dakika önce)\n" +
                   "- com.example:auth-service:jar:2.1.0 (Yüklendi: 1 saat önce)";
        } else if (cleanedRepo.contains("docker") || cleanedRepo.contains("imaj")) {
            return "Nexus Raporu [docker-registry]:\n" +
                   "- mcp-gateway:latest (SHA-256 doğrulandı, Yüklendi: 5 dakika önce)\n" +
                   "- payment-service:v1.4 (Yüklendi: dün)";
        }

        // Eğer kullanıcı farklı/bilinmeyen bir depo ismi gönderirse
        return "Nexus Raporu [" + repositoryName + "]: Bu depoya ait son 5 dakika içinde yeni bir artifact yüklemesi bulunamadı.";
    }
}