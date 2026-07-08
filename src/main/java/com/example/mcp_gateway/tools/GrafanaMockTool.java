package com.example.mcp_gateway.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class GrafanaMockTool {

    @Tool("Sistemdeki CPU ve Bellek (Memory) gibi kaynak tüketim metriklerini sorgular.")
    public String getSystemMetrics(String metricType) {
        System.out.println("[MCP GATEWAY LOG] GrafanaMockTool tetiklendi: getSystemMetrics() -> " + metricType);
        
        // 🛠️ 1. ADIM: NULL VEYA BOŞ DEĞER KONTROLÜ (Güvenlik Duvarı)
        if (metricType == null || metricType.trim().isEmpty()) {
            return "Hata: Sorgulanacak metrik türü tespit edilemedi. Lütfen 'CPU' veya 'Bellek' belirtin.";
        }

        String cleanedType = metricType.trim();

        // 🛠️ 2. ADIM: TAM EŞLEŞME VE GEÇERLİLİK KONTROLLERİ
        if ("CPU".equalsIgnoreCase(cleanedType)) {
            return "Grafana Metrik Raporu: Ortalama CPU Kullanımı %85 seviyesinde. (Kritik Eşik Aşıldı!)";
        } else if ("Bellek".equalsIgnoreCase(cleanedType) || "Memory".equalsIgnoreCase(cleanedType)) {
            return "Grafana Metrik Raporu: Bellek Kullanımı %55 seviyesinde. (Normal)";
        }

        // 🛠️ 3. ADIM: DESTEKLENMEYEN SAÇMA BİR PARAMETRE GELDİĞİNDE
        return "Hata: '" + metricType + "' geçerli bir metrik değildir. Grafana sadece 'CPU' veya 'Bellek' metriklerini destekler.";
    }
}