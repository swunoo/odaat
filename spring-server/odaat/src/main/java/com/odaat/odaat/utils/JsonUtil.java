package com.odaat.odaat.utils;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Custom simple utility class to parse Json responses from APIs.
 */
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Object deserializeJson(String jsonString) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        if (jsonNode.isObject()) {
            return objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {});
        } else if (jsonNode.isArray()) {
            return objectMapper.convertValue(jsonNode, new TypeReference<List<Object>>() {});
        } else if (jsonNode.isTextual()) {
            return jsonNode.asText();
        } else {
            throw new IllegalArgumentException("Unsupported JSON format");
        }
    }

}