package fr.univparis1.opilaissezpasser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author ebohm
 */
public class PropertySingleton {

    private Properties properties;

    private PropertySingleton() {
        InputStream input = null;

        try {

            String filename = "configUrlServices.properties";
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                return;
            }

            properties = new Properties();

            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static PropertySingleton getInstance() {
        return PropertySingletonHolder.INSTANCE;
    }

    private static class PropertySingletonHolder {
        private static final PropertySingleton INSTANCE = new PropertySingleton();
    }

    /**
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }
}
