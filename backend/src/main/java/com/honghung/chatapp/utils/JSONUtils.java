package com.honghung.chatapp.utils;  
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class JSONUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T getPropertyFromJson(String jsonString, String propertyName, Class<T> clazz) {
        try {
            // Phân tích chuỗi JSON thành JsonNode
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            // Lấy giá trị của thuộc tính theo tên
            JsonNode propertyNode = jsonNode.get(propertyName);
            // Chuyển đổi giá trị thành kiểu T
            return objectMapper.treeToValue(propertyNode, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertToObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertToJSON(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> readJSONString(String message) {
        try {
            return objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return null;
        }
    }
}
