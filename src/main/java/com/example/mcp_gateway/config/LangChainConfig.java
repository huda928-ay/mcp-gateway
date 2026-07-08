package com.example.mcp_gateway.config;

import com.example.mcp_gateway.tools.GrafanaMockTool;
import com.example.mcp_gateway.tools.KubernetesMockTool;
import com.example.mcp_gateway.tools.NexusMockTool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChainConfig {

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        // Ücretsiz testler için LangChain4j'nin sağladığı genel demo anahtarını kullanıyoruz
        return OpenAiChatModel.builder()
                .apiKey("demo") 
                .modelName("gpt-4o-mini")
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    // LLM ile konuşurken aracı rolü üstlenecek olan Akıllı Ajan Arayüzü
    public interface McpAgent {
        String ask(String userQuery);
    }

   @Bean
public McpAgent mcpAgent(ChatLanguageModel model, 
                         KubernetesMockTool k8sTool, 
                         GrafanaMockTool grafanaTool,
                         NexusMockTool nexusTool) { // 1. Parametre olarak ekledik
    return AiServices.builder(McpAgent.class)
            .chatLanguageModel(model)
            .tools(k8sTool, grafanaTool, nexusTool) // 2. Alet çantasına ekledik
            .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
            .build();
}
}