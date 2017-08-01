package com.github.schmeedy.zonky;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    private static ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public static <E> E getJsonResource(String path, Class<E> type) {
        try (InputStream in = TestUtils.class.getResourceAsStream(path)) {
            return objectMapper.readValue(in, type);
        } catch (IOException e) {
            throw new RuntimeException("test resource not found", e);
        }
    }

    private TestUtils() {}
}
