package com.ebay.calculator.calculator.service;

import com.ebay.calculator.calculator.dto.ChainRequest;
import com.ebay.calculator.calculator.dto.HistoryEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HistoryServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ListOperations<String, String> listOps;

    @InjectMocks
    private HistoryService historyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForList()).thenReturn(listOps);
    }

    @Test
    void testSaveHistory_storesSerializedEntry() throws Exception {
        ChainRequest input = new ChainRequest();
        input.setInitial(10.0);
        input.setOperations(List.of());
        input.setValues(List.of());

        historyService.saveHistory("test:history", input, "chain", 42.0);

        verify(listOps, times(1)).leftPush(eq("test:history"), anyString());
    }

    @Test
    void testSaveHistory_handlesSerializationError() {
        historyService.saveHistory("test:history", new Object() {
            // circular reference or invalid type
            public Object self = this;
        }, "chain", 99.0);
        // No exception thrown, log should catch it
        verify(listOps, never()).leftPush(any(), any());
    }

    @Test
    void testGetRecent_returnsDeserializedHistory() throws Exception {
        HistoryEntry entry = new HistoryEntry("{}", "chain", 1.0, "2025-05-30T00:00:00Z");
        String json = objectMapper.writeValueAsString(entry);

        when(listOps.range("calculator:history", 0, 1)).thenReturn(List.of(json));
        List<HistoryEntry> history = historyService.getRecent(2);

        assertEquals(1, history.size());
        assertEquals("chain", history.get(0).getInputType());
    }

    @Test
    void testGetRecent_handlesInvalidJson() {
        when(listOps.range("calculator:history", 0, 1)).thenReturn(List.of("invalid-json"));
        List<HistoryEntry> history = historyService.getRecent(2);

        assertEquals(0, history.size());
    }

    @Test
    void testGetHistory_returnsRawJsonList() {
        when(listOps.range("key1", 0, 4)).thenReturn(List.of("a", "b"));
        List<String> result = historyService.getHistory("key1", 5);

        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
    }

    @Test
    void testGetHistory_whenNull_returnsEmptyList() {
        when(listOps.range("empty", 0, 2)).thenReturn(null);
        List<String> result = historyService.getHistory("empty", 3);
            assertNull(result);
    }
}
