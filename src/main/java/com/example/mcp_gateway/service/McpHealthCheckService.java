package com.example.mcp_gateway.service;

import com.example.mcp_gateway.model.McpServerConfig;
import com.example.mcp_gateway.repository.McpServerConfigRepository;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class McpHealthCheckService {

    private final McpServerConfigRepository repository; 
    private final RestTemplate restTemplate;

    // Constructor içinde RestTemplate'i timeout ayarlarıyla donatıyoruz
    public McpHealthCheckService(McpServerConfigRepository repository) {
        this.repository = repository;
        
        // İstek fabrikasını oluşturup zaman aşımı sınırlarını belirliyoruz
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // 3 saniye içinde sunucuya hiç bağlanamazsa pes et (Connect Timeout)
        factory.setConnectTimeout(3000); 
        
        // Bağlantı kurulduktan sonra 3 saniye içinde veri gelmezse isteği kes (Read Timeout)
        factory.setReadTimeout(3000);   

        // Artık restTemplate nesnemiz koruma kalkanına sahip
        this.restTemplate = new RestTemplate(factory); 
    }

    // Görev 1: 10 saniyede bir (10000 ms) otomatik çalışacak zamanlayıcı
    @Scheduled(fixedRate = 10000)
    public void checkServersHealth() {
        System.out.println("--- Sağlık Kontrolü Başlatıldı: " + LocalDateTime.now() + " ---");

        // DB'deki tüm sunucuları listeliyoruz
        List<McpServerConfig> servers = repository.findAll();

        for (McpServerConfig server : servers) {
            String url = server.getEndpointUrl();
            
            // Eğer URL boşsa kontrol etmeye gerek yok, pas geç
            if (url == null || url.isBlank()) {
                continue;
            }

            try {
                // Görev 2: Sunucu URL'ine HTTP GET isteği atıyoruz
                // Not: Eğer hedef sunucu 3 saniye içinde yanıt vermezse, bu satır otomatik olarak
                // ResourceAccessException fırlatacak ve doğrudan catch bloğuna zıplayacaktır.
                restTemplate.getForEntity(url, String.class);
                
                // İstek başarılı olduysa durumu bellekte UP yapıyoruz
                server.setStatus("UP"); 
                System.out.println("Sunucu [" + server.getId() + "] - " + url + " DURUM: UP");

            } catch (Exception e) {
                // Timeout veya sunucunun tamamen kapalı olma (Connection Refused) durumu buraya düşer
                server.setStatus("DOWN");
                System.out.println("Sunucu [" + server.getId() + "] - " + url + " DURUM: DOWN (Hata: " + e.getMessage() + ")");
            }

            // --- YARIŞ DURUMU (RACE CONDITION) ÇÖZÜMÜ ---
            // Harika kurgulanmış koruma mekanizman aynen kalıyor.
            McpServerConfig currentDbServer = repository.findById(server.getId()).orElse(server);
            
            currentDbServer.setStatus(server.getStatus());
            currentDbServer.setLastChecked(LocalDateTime.now());
            
            repository.saveAndFlush(currentDbServer);
        }
        
        System.out.println("--- Sağlık Kontrolü Bitti ---");
    }
}