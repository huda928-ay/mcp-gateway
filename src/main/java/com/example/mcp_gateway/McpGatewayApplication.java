package com.example.mcp_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Zamanlayıcı mekanizmasını aktif eder
public class McpGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpGatewayApplication.class, args);
	}

}
