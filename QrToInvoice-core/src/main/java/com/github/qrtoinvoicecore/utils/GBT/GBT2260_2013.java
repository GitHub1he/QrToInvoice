package com.github.qrtoinvoicecore.utils.GBT;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GBT2260_2013 {
    private static final GBT2260_2013 INSTANCE = new GBT2260_2013();
    private final Map<Integer, String> dictionary;

    private GBT2260_2013() {
        dictionary = new HashMap<>();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("areas.properties")) {
            if (input == null) {
                throw new RuntimeException("areas.properties file not found in classpath");
            }

            // 使用 UTF-8 编码读取
            InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            Properties props = new Properties();
            props.load(reader);

            for (String key : props.stringPropertyNames()) {
                dictionary.put(Integer.parseInt(key), props.getProperty(key));
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load areas.properties", e);
        }
    }

    public static GBT2260_2013 getInstance() {
        return INSTANCE;
    }

    public Map<Integer, String> getDictionary() {
        return new HashMap<>(dictionary);
    }

    public String getAreaName(Integer code) {
        return dictionary.get(code);
    }

    public boolean containsCode(Integer code) {
        return dictionary.containsKey(code);
    }

    public Set<Integer> getAllCodes() {
        return new HashSet<>(dictionary.keySet());
    }
}
