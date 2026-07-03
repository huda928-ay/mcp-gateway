package com.example.mcp_gateway.config;

import com.example.mcp_gateway.model.McpServerConfig;
import com.example.mcp_gateway.repository.McpServerConfigRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final McpServerConfigRepository repository;

    // Bağımlılık Enjeksiyonu (Dependency Injection): Spring bize repository nesnesini getirir
    public DataInitializer(McpServerConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Görevde bizden istenen 5 zorunlu sunucu
        String[] defaultServers = {"Kubernetes", "Grafana", "Nexus", "MAAS", "Workflow"};
        
        Arrays.stream(defaultServers).forEach(serverName -> {
            // Eğer bu sunucu veritabanında zaten yoksa ekle (Mükerrer kaydı önler)
            if (!repository.existsByServerName(serverName)) {
                McpServerConfig config = McpServerConfig.builder()
                        .serverName(serverName)
                        // Testlerimizin başlangıçta başarılı simüle edilmesi için mock bir adres veriyoruz
                        .endpointUrl("https://httpbin.org/status/200")
                        .status("UNKNOWN") // İlk başta durum bilinmiyor, scheduler kontrol edecek
                        .lastChecked(LocalDateTime.now())
                        .build();
                
                repository.save(config); // Veritabanına kaydetme anı!
            }
        });
        
        System.out.println(">> [Data Seeding] 5 Zorunlu MCP Sunucusu Veritabanına Başarıyla Eklendi.");
    }
}