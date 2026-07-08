package com.example.mcp_gateway.controller;

import com.example.mcp_gateway.service.McpAgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
@CrossOrigin(origins = "*") // Frontend entegrasyonunda CORS hatası almamak için şimdiden ekledik
public class AgentController {

    private final McpAgentService mcpAgentService;

    public AgentController(McpAgentService mcpAgentService) {
        this.mcpAgentService = mcpAgentService;
    }

    @PostMapping("/query")
    public ResponseEntity<String> queryAgent(@RequestBody String query) {
        String response = mcpAgentService.processNaturalLanguageQuery(query);
        return ResponseEntity.ok(response);
    }
}