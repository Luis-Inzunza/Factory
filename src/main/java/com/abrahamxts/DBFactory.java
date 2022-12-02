package com.abrahamxts;

import java.util.Properties;
import com.abrahamxts.util.*;
import com.abrahamxts.adapters.*;

public class DBFactory {

    public static IDBAdapter getDBadapter(DBType dbType) {
        switch (dbType) {
            case MySQL:
                return new MySQLDBAdapter();
            case PostgreSQL:
                return new PostgreSQLDBAdapter();
            default:
                throw new IllegalArgumentException("Base de datos no soportada.");
        }
    }

    public static IDBAdapter getDefaultDBAdapter() {
        try {
            Properties properties = PropertiesUtil.loadProperty("META-INF/DBFactory.properties");

            String defaultDBClass = properties.getProperty("DB_CLASS");

            System.out.println("Clase de datos por defecto ==> " + defaultDBClass);
			
            return (IDBAdapter) Class.forName(defaultDBClass).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
			System.out.println("No se ha podido acceder al archivo de propiedades: " + e.getMessage());
            return null;
        }
    }
}