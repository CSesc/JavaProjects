package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyReader {

    private  static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(new File("application.properties")));


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key,"Not found");
    }
}
