package com.example.mcp_gateway.controller;

import com.example.mcp_gateway.model.McpServerConfig;
import com.example.mcp_gateway.service.McpServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mcp-servers")
@CrossOrigin(origins = "*") // İleride frontend paneli bağlanırken CORS hatası almamak için güvenlik izni
public class McpServerController {

    private final McpServerService service;

    public McpServerController(McpServerService service) {
        this.service = service;
    }

    // Görev 1: GET /api/mcp-servers -> Tüm sunucuları listeler
    @GetMapping
    public ResponseEntity<List<McpServerConfig>> getAllServers() {
        List<McpServerConfig> servers = service.getAllServers();
        return ResponseEntity.ok(servers); // HTTP 200 Başarılı kodu ile listeyi JSON olarak döner
    }

    // Görev 2: PUT /api/mcp-servers/{id} -> Belirli bir sunucunun URL'ini günceller
    @PutMapping("/{id}")
    public ResponseEntity<McpServerConfig> updateServerUrl(
            @PathVariable Long id, 
            @RequestBody Map<String, String> requestBody) {
        
        String newUrl = requestBody.get("endpointUrl");
        
        // Basit bir veri doğrulama (Validation) kontrolü
        if (newUrl == null || newUrl.isBlank()) {
            return ResponseEntity.badRequest().build(); // Eğer URL boş gönderildiyse HTTP 400 Hatalı İstek dön
        }
        
        McpServerConfig updatedServer = service.updateServerUrl(id, newUrl);
        return ResponseEntity.ok(updatedServer); // Güncellenmiş yeni halini HTTP 200 ile geri dön
    }
}