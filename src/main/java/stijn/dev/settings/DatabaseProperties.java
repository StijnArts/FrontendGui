package stijn.dev.settings;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class DatabaseProperties {
    private Logger logger = Logger.getLogger(DatabaseProperties.class.getName());
    private Properties properties;
    private InputStream filePath = DatabaseProperties.class.getClassLoader().getResourceAsStream("database.properties");
    public DatabaseProperties(){
        properties = new Properties();
        try {
            properties.load(filePath);
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
        try {
            properties.store(new FileOutputStream("C:\\Users\\Stijn\\Desktop\\Front End project\\FrontendGui\\src\\main\\resources\\database.properties"),null);
            filePath.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
