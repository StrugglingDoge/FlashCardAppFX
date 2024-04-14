package com.flashcardapp.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.flashcardapp.model.Flashcard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the configuration of the FlashcardApp.
 */
public class ConfigHandler {
    private static ConfigHandler instance;
    private Map<String, Object> configMap;
    private final ObjectMapper mapper;
    private static final String CONFIG_PATH = System.getProperty("user.home") + File.separator + "flashcardapp" + File.separator + "config.json";

    /**
     * Private constructor for initializing the ObjectMapper and loading the existing configuration.
     */
    private ConfigHandler() {
        mapper = createMapper();
        loadConfig();
    }

    /**
     * Creates and configures an ObjectMapper for JSON serialization and deserialization.
     * @return Configured ObjectMapper.
     */
    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(Flashcard.class, new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) {
                return new Flashcard(key, "");
            }
        });
        mapper.registerModule(module);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    /**
     * Provides the singleton instance of ConfigHandler.
     * @return The singleton instance of ConfigHandler.
     */
    public static synchronized ConfigHandler getInstance() {
        if (instance == null) {
            instance = new ConfigHandler();
        }
        return instance;
    }

    /**
     * Loads the configuration from a predefined JSON file path.
     */
    private void loadConfig() {
        try {
            File configFile = new File(CONFIG_PATH);
            if (configFile.exists()) {
                configMap = mapper.readValue(configFile, new TypeReference<Map<String, Object>>() {});
            } else {
                configMap = new HashMap<>();
            }
        } catch (IOException e) {
            configMap = new HashMap<>();
            e.printStackTrace();
        }
    }

    /**
     * Saves a configuration option.
     * @param key The key under which the option will be saved.
     * @param value The value of the option to save.
     */
    public void saveOption(String key, Object value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }
        configMap.put(key, value);
        saveConfig();
    }

    /**
     * Retrieves a configuration option.
     * @param key The key of the configuration option to retrieve.
     * @param type The expected type of the configuration option.
     * @return The value of the configuration option, cast to the expected type.
     * @throws IllegalArgumentException if the key is not found or cannot be cast to the expected type.
     */
    public <T> T getOption(String key, Class<T> type) {
        Object data = configMap.get(key);
        if (data == null) {
            throw new IllegalArgumentException("Option " + key + " not found in config.");
        }
        return mapper.convertValue(data, type);
    }

    /**
     * Saves the current configuration map to the predefined JSON file path.
     */
    private void saveConfig() {
        try {
            mapper.writeValue(Paths.get(CONFIG_PATH).toFile(), configMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manually reloads the configuration from the file.
     */
    public void reloadConfiguration() {
        loadConfig();
    }
}