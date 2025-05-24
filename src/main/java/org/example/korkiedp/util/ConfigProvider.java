package org.example.korkiedp.util;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class ConfigProvider {

    private static final PropertiesConfiguration config;
    private static final String filepath = "src/main/resources/config.properties";
    static {
        Configurations configs = new Configurations();
        try {
            System.out.println("Loading configuration file...");
            config = configs.properties(new File(filepath));
        } catch (ConfigurationException e) {
            throw new RuntimeException("Could not load configuration", e);
        }
    }

    public static Object get(String key) {
        return config.getProperty(key);
    }
    public static void set(String key, Object value) {
        config.setProperty(key, value);
        save();
    }

    public static void clear(String key) {
        config.clearProperty(key);
        save();
    }

    public static boolean isSet(String key) {
        return (config.getProperty(key) != null);
    }
    private static void save(){
        try {
            config.write(new java.io.FileWriter(filepath));
        } catch (Exception e) {
            throw new RuntimeException("Could not save configuration", e);

        }
    }
}
