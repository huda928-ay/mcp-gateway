package com.example.mcp_gateway.service;

import com.example.mcp_gateway.model.AgenticWorkflow;
import com.example.mcp_gateway.model.McpServerConfig;
import com.example.mcp_gateway.repository.AgenticWorkflowRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AgenticWorkflowService {

    private final AgenticWorkflowRepository workflowRepository;
    private final McpServerService mcpServerService; // ◄ Sunucu kontrolü için servis buraya eklendi

    public AgenticWorkflowService(AgenticWorkflowRepository workflowRepository, 
                                  McpServerService mcpServerService) {
        this.workflowRepository = workflowRepository;
        this.mcpServerService = mcpServerService;
    }

    // 1. Yeni Bir İş Akışı Kaydetme (Girdi Kontrollü)
    public AgenticWorkflow saveWorkflow(AgenticWorkflow workflow) {
        if (workflow.getWorkflowName() == null || workflow.getWorkflowName().trim().isEmpty()) {
            throw new IllegalArgumentException("İş akışı ismi (workflowName) boş bırakılamaz!");
        }
        if (workflow.getSteps() == null || workflow.getSteps().isEmpty()) {
            throw new IllegalArgumentException("İş akışı en az bir sıralı adım (step) içermelidir!");
        }
        return workflowRepository.save(workflow);
    }

    // 2. Tüm İş Akışlarını Listeleme
    public List<AgenticWorkflow> getAllWorkflows() {
        return workflowRepository.findAll();
    }

    /**
     * 🎯 YENİ İŞ MANTIĞI METODU (Dinamik Engel)
     * Gelen adım açıklamasında DOWN durumunda olan bir sunucunun adı geçiyorsa kuralı ihlal eder ve hata fırlatır.
     */
    public void checkDownServersRestrictions(String stepDescription) {
        // Veritabanındaki tüm DOWN durumundaki sunucuları çekiyoruz
        List<McpServerConfig> downServers = mcpServerService.getAllServers().stream()
                .filter(server -> "DOWN".equalsIgnoreCase(server.getStatus()))
                .toList();

        // Eğer istem (prompt) DOWN olan herhangi bir sunucunun adını içeriyorsa engelle!
        for (McpServerConfig downServer : downServers) {
            if (stepDescription.toLowerCase().contains(downServer.getServerName().toLowerCase())) {
                System.err.println("-> [ENGELLENDİ] " + downServer.getServerName() + " sunucusu DOWN olduğu için akış kesildi.");
                throw new IllegalArgumentException("Hata: '" + downServer.getServerName() + "' sunucusu şu an DOWN durumda! Bu adım yürütülemez.");
            }
        }
    }
}