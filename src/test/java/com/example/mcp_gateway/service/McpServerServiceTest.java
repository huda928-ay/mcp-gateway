package com.example.mcp_gateway.service;

import com.example.mcp_gateway.model.McpServerConfig;
import com.example.mcp_gateway.repository.McpServerConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class McpServerServiceTest {

    @Mock
    private McpServerConfigRepository repository;

    @InjectMocks
    private McpServerService serverService; 

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateServerUrl_Success() {
        // Given (Ön Hazırlık)
        Long serverId = 1L;
        String oldUrl = "https://httpbin.org/status/200";
        String newUrl = "https://httpbin.org/status/500";

        McpServerConfig mockServer = new McpServerConfig();
        mockServer.setId(serverId);
        mockServer.setEndpointUrl(oldUrl);
        mockServer.setStatus("UP");

        when(repository.findById(serverId)).thenReturn(Optional.of(mockServer));
        when(repository.saveAndFlush(any(McpServerConfig.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When (Aksiyon)
        McpServerConfig result = serverService.updateServerUrl(serverId, newUrl);

        // Then (Doğrulama)
        assertNotNull(result);
        assertEquals(newUrl, result.getEndpointUrl());
        assertEquals("UNKNOWN", result.getStatus()); // Durum sıfırlanmalı
        verify(repository, times(1)).findById(serverId);
        verify(repository, times(1)).saveAndFlush(mockServer);
    }

    @Test
    void testUpdateServerUrl_ServerNotFound() {
        // Given
        Long invalidId = 99L;
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            serverService.updateServerUrl(invalidId, "https://test.com");
        });

        verify(repository, times(1)).findById(invalidId);
        verify(repository, never()).saveAndFlush(any());
    }
}
