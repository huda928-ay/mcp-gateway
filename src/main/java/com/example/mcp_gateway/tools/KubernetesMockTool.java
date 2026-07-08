package com.example.mcp_gateway.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class KubernetesMockTool {

    @Tool("Kubernetes kümesindeki aktif podları ve onların anlık durumlarını listeler.")
    public String listPods() {
        System.out.println("[MCP GATEWAY LOG] KubernetesMockTool tetiklendi: listPods()");
        return "Pod Name: auth-service-xyz, Status: RUNNING, Restarts: 0\n" +
               "Pod Name: payment-service-abc, Status: CRASH_LOOP_BACKOFF, Restarts: 14";
    }

    @Tool("Belirtilen pod ismine göre yeni bir replika konuşlandırır veya podu yeniden başlatır.")
    public String deployNewPod(String podName) {
        System.out.println("[MCP GATEWAY LOG] KubernetesMockTool tetiklendi: deployNewPod() -> " + podName);
        
        // VALİDASYON BURASI:
        if (podName == null || podName.trim().isEmpty() || podName.equalsIgnoreCase("null")) {
            return "Hata: Yeniden başlatılacak pod ismi belirtilmedi veya anlaşılamadı. Lütfen geçerli bir pod ismi verin (Örn: payment-service-abc).";
        }

        return "Başarılı: " + podName + " için yeni bir replika başarıyla ayağa kaldırıldı ve trafik almaya hazır.";
    }
}