package kamel.servletmvc.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;

public class JsonReader {
    public static JsonNode getNode(BufferedReader requestBodyReader) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = requestBodyReader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String jsonData = jsonBuilder.toString();

        // Parse JSON using Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonData);
    }
}
