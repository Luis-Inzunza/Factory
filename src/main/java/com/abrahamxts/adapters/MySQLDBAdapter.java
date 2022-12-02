package com.abrahamxts.adapters;

import java.sql.*;
import java.util.Properties;

import com.abrahamxts.IDBAdapter;
import com.abrahamxts.util.PropertiesUtil;

public class MySQLDBAdapter implements IDBAdapter {

    private static final String DB_PROPERTIES = "META-INF/DBMySQL.properties";

    static {
        try {
            new com.mysql.jdbc.Driver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        try {
            String connectionString = createConnectionString();
            Connection connection = DriverManager.getConnection(connectionString);

            System.out.println("Clase de conexión ==> " + connection.getClass().getName());

            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String createConnectionString() {
        Properties properties = PropertiesUtil.loadProperty(DB_PROPERTIES);
		
        String host = properties.getProperty("HOST");
        String port = properties.getProperty("PORT");
        String db = properties.getProperty("DATABASE");
        String user = properties.getProperty("USER");
        String password = properties.getProperty("PASSWORD");

        String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + password;
        
		System.out.println("String de conexión ==> " + connectionString);
        
		return connectionString;
    }
}