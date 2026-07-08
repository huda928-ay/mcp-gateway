package com.example.mcp_gateway.service;

import com.example.mcp_gateway.config.LangChainConfig.McpAgent;
import com.example.mcp_gateway.model.McpServerConfig; // Sunucu modelini içeri aktarıyoruz
import com.example.mcp_gateway.repository.McpServerConfigRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class McpAgentService {

    private final McpAgent mcpAgent;
    private final McpServerConfigRepository mcpServerConfigRepository;

    public McpAgentService(McpAgent mcpAgent, McpServerConfigRepository mcpServerConfigRepository) {
        this.mcpAgent = mcpAgent;
        this.mcpServerConfigRepository = mcpServerConfigRepository;
    }

    public String processNaturalLanguageQuery(String query) {
        System.out.println("[MCP GATEWAY] Doğal dil sorgusu işleniyor: " + query);
        
        String queryLower = query.toLowerCase();

        // 🛡️ JENERİK KONTROL: Veritabanındaki tüm DOWN (pasif) durumundaki sunucuları çekiyoruz
        List<McpServerConfig> downServers = mcpServerConfigRepository.findAll().stream()
            .filter(server -> "DOWN".equalsIgnoreCase(server.getStatus()))
            .toList();

        // Kullanıcının chatbot'a yazdığı sorgu, DOWN olan herhangi bir sunucunun adını içeriyorsa engelle!
        for (McpServerConfig downServer : downServers) {
            if (queryLower.contains(downServer.getServerName().toLowerCase())) {
                System.err.println("-> [SORGU ENGELLENDİ] " + downServer.getServerName() + " sunucusu DOWN durumda.");
                return "Üzgünüm, '" + downServer.getServerName() + "' sunucusu şu an DOWN (Kapalı) durumda olduğu için bu işlemi gerçekleştiremiyorum. Lütfen altyapı bağlantısını kontrol edin.";
            }
        }

        // Eğer ilgili sunucu ayaktaysa (UP) veya genel bir soruysa ajanın (LLM) cevaplamasına izin ver
        return mcpAgent.ask(query);
    }
}