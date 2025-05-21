package org.example.korkiedp.util;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;

public class PasswordUtil {

    private static final PropertiesConfiguration config;

    static {
        Configurations configs = new Configurations();
        try {
            System.out.println("Loading configuration file...");
            config = configs.properties(new File("src/main/resources/config.properties"));
        } catch (ConfigurationException e) {
            throw new RuntimeException("Could not load configuration", e);
        }

    }

    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(Integer.parseInt(config.getString("bcrypt.workload"))));
    }

    public static boolean check(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) return false;
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

