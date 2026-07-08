package com.example.mcp_gateway.service;

import com.example.mcp_gateway.model.McpServerConfig;
import com.example.mcp_gateway.repository.McpServerConfigRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class McpServerService {

    private final McpServerConfigRepository repository;

    // Bağımlılık Enjeksiyonu: Spring arka plandaki repository'yi buraya bağlar
    public McpServerService(McpServerConfigRepository repository) {
        this.repository = repository;
    }

    // 1. Tüm sunucuları listeleme iş mantığı (GET isteği için)
    public List<McpServerConfig> getAllServers() {
        return repository.findAll(); // Repository sayesinde "SELECT * FROM..." sorgusu çalışır
    }

    // 2. Bir sunucunun URL bilgisini runtime'da güncelleme iş mantığı (PUT isteği için)
    public McpServerConfig updateServerUrl(Long id, String newUrl) {
        // Veritabanında bu ID'ye sahip bir sunucu var mı kontrol et, yoksa hata fırlat
        McpServerConfig config = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sunucu bulunamadı! ID: " + id));

        config.setEndpointUrl(newUrl); // Yeni URL'i nesneye set et
        config.setStatus("UNKNOWN"); // URL değiştiği için durumunu sıfırlıyoruz, birazdan scheduler kontrol edecek

        // DEĞİŞEN YER: save yerine saveAndFlush kullanıyoruz!
        McpServerConfig updatedConfig = repository.saveAndFlush(config); 
        
        return updatedConfig;
    }

    // 3. Yeni bir sunucu ekleme iş mantığı (POST isteği için)
    public McpServerConfig createServer(McpServerConfig serverConfig) {
        // Yeni sunucu eklenirken başlangıç durumunu UNKNOWN yapıyoruz, scheduler tıkır tıkır kontrol edecek
        serverConfig.setStatus("UNKNOWN");
        serverConfig.setLastChecked(LocalDateTime.now());
        
        // Yarış durumlarına takılmamak ve veritabanına anında yazılmasını garanti etmek için saveAndFlush kullanıyoruz
        return repository.saveAndFlush(serverConfig);
    }
}