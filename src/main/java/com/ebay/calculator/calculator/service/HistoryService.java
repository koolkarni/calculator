package com.ebay.calculator.calculator.service;

import com.ebay.calculator.calculator.dto.HistoryEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String HISTORY_KEY = "calculator:history";

    public void saveHistory(String key, Object input, String inputType, double result) {
        try {
            HistoryEntry entry = new HistoryEntry();
            entry.setInput(input); // Store as raw object, NOT JSON string
            entry.setInputType(inputType);
            entry.setResult(result);
            entry.setTimestamp(Instant.now().toString());

            String json = objectMapper.writeValueAsString(entry);
            redisTemplate.opsForList().leftPush(key, json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize history entry", e);
        }
    }

    public List<HistoryEntry> getRecent(int count) {
        List<String> entries = redisTemplate.opsForList().range(HISTORY_KEY, 0, count - 1);
        if (entries == null) return List.of();

        return entries.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, HistoryEntry.class);
                    } catch (JsonProcessingException e) {
                        log.error("Failed to deserialize history entry", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<String> getHistory(String key, int count) {
        return redisTemplate.opsForList().range(key, 0, count - 1);
    }
}
