package com.example.mcp_gateway.controller;

import com.example.mcp_gateway.model.AgenticWorkflow;
import com.example.mcp_gateway.service.AgenticWorkflowService;
import com.example.mcp_gateway.service.McpAgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/workflows")
public class AgenticWorkflowController {

    private final AgenticWorkflowService workflowService;
    private final McpAgentService mcpAgentService;

    // ◄ McpServerService buradan kaldırıldı, kod sadeleşti
    public AgenticWorkflowController(AgenticWorkflowService workflowService, 
                                     McpAgentService mcpAgentService) {
        this.workflowService = workflowService;
        this.mcpAgentService = mcpAgentService;
    }

    @PostMapping
    public ResponseEntity<?> createWorkflow(@RequestBody AgenticWorkflow workflow) {
        try {
            AgenticWorkflow saved = workflowService.saveWorkflow(workflow);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AgenticWorkflow>> getAllWorkflows() {
        return ResponseEntity.ok(workflowService.getAllWorkflows());
    }

    @PostMapping("/execute-step")
    public ResponseEntity<String> executeWorkflowStep(@RequestBody String stepDescription) {
        System.out.println("\n--- [TASK 5 LOG] JENERİK İŞ AKIŞI ADIMI BAŞLADI ---");
        
        try {
            // 1. İş mantığı kontrolü artık Servis katmanında yapılıyor
            workflowService.checkDownServersRestrictions(stepDescription);

            // 2. Sunucular ayakta ise ajana pasla
            String agentResponse = mcpAgentService.processNaturalLanguageQuery(stepDescription);
            
            // Jenerik Kontrol 2: Ajanın cevabında hata kontrolü
            if (agentResponse.toLowerCase().contains("bağlantı hatası") || agentResponse.toLowerCase().contains("sunucuya erişilemedi")) {
                return ResponseEntity.status(500).body("Ajan Hatası: " + agentResponse);
            }

            return ResponseEntity.ok(agentResponse);

        } catch (IllegalArgumentException e) {
            // Servis katmanından fırlatılan kural ihlali hatalarını burada yakalayıp 400 Bad Request dönüyoruz
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Sistem Hatası: " + e.getMessage());
        }
    }
}