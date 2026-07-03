package com.example.mcp_gateway.service;

import com.example.mcp_gateway.model.McpServerConfig;
import com.example.mcp_gateway.repository.McpServerConfigRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class McpHealthCheckService {

    private final McpServerConfigRepository repository; 
    private final RestTemplate restTemplate;

    public McpHealthCheckService(McpServerConfigRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate(); 
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
                restTemplate.getForEntity(url, String.class);
                
                // İstek başarılı olduysa durumu bellekte UP yapıyoruz
                server.setStatus("UP"); 
                System.out.println("Sunucu [" + server.getId() + "] - " + url + " DURUM: UP");

            } catch (Exception e) {
                // Hata durumunda durumu bellekte DOWN yapıyoruz
                server.setStatus("DOWN");
                System.out.println("Sunucu [" + server.getId() + "] - " + url + " DURUM: DOWN (Hata: " + e.getMessage() + ")");
            }

            // --- YARIŞ DURUMU (RACE CONDITION) ÇÖZÜMÜ ---
            // Tam kaydetme anında, bu sunucunun veritabanındaki EN GÜNCEL halini çekiyoruz.
            // Eğer sen döngü çalışırken arayüzden URL değiştirdiysen, veritabanındaki o yeni URL'i yakalıyoruz.
            McpServerConfig currentDbServer = repository.findById(server.getId()).orElse(server);
            
            // Sadece test ettiğimiz durumu (status) ve zaman damgasını güncel tutuyoruz.
            // Böylece kullanıcının arayüzden yazdığı yeni URL'i asla ESKİSİYLE EZMİYORUZ!
            currentDbServer.setStatus(server.getStatus());
            currentDbServer.setLastChecked(LocalDateTime.now());
            
            // saveAndFlush kullanarak verinin H2 veritabanına anında yazılmasını garanti ediyoruz.
            repository.saveAndFlush(currentDbServer);
        }
        
        System.out.println("--- Sağlık Kontrolü Bitti ---");
    }
}