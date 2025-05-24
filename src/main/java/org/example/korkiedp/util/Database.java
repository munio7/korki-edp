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

    public static Connection getConnection() throws SQLException {
        String url = (String) ConfigProvider.get("db.url");
        String username = (String) ConfigProvider.get("db.user");
        String password = (String) ConfigProvider.get("db.password");

        return DriverManager.getConnection(url, username, password);
    }
}
