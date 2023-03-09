package stijn.dev.settings;

import stijn.dev.data.database.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class DatabaseProperties {
    private Logger logger = Logger.getLogger(DatabaseProperties.class.getName());
    private Properties properties;
    public DatabaseProperties(){
        properties = new Properties();
        try {
            properties.load(DatabaseProperties.class.getClassLoader().getResourceAsStream("database.properties"));
        } catch (
                IOException e) {
            logger.log(Level.SEVERE, "Can't access property file database.properties");
        }
    }

    public boolean isInitialized(){
        return Boolean.valueOf(properties.getProperty("isInitialized"));
    }

    public void isInitialized(boolean state){
        properties.setProperty("isInitialized",String.valueOf(state));
    }

}
