package io.sahka;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Alexander Yushchenko
 * @since 2017.04.15
 */
public class Configuration {

    private static Boolean enabled;

    public static boolean isEnabled() {
        if (enabled == null){
            loadFromProperties();
        }
        return enabled;
    }
REMOVEME
    private static void loadFromProperties() {
        Properties properties = readProperties();
        enabled = "true".equals(properties.getProperty("enabled"));
    }

    private static Properties readProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("some.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
