package org.example.korkiedp.util;

import java.io.File;
import java.lang.module.Configuration;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
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

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.getString("db.url"), config.getString("db.user"), config.getString("db.password"));
    }
}
