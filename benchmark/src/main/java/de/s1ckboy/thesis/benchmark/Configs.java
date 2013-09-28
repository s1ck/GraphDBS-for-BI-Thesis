package de.s1ckboy.thesis.benchmark;

import java.util.HashMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Singleton class to load a properties file by filename and read properties by
 * property name.
 */
public class Configs {
    private static HashMap<String, PropertiesConfiguration> instances = new HashMap<String, PropertiesConfiguration>();

    /**
     * Returns a instance to the configuration file.
     * 
     * @param instanceName
     * @return
     */
    public static Configuration get(String instanceName) {
	if (instances.get(instanceName) == null) {
	    try {
		instances.put(instanceName, new PropertiesConfiguration(
			getFilename(instanceName)));
	    } catch (ConfigurationException e) {
		e.printStackTrace();
	    }
	}
	return instances.get(instanceName);
    }

    private static String getFilename(String instanceName) {
	return "src/main/resources/" + instanceName + ".properties";
    }
}