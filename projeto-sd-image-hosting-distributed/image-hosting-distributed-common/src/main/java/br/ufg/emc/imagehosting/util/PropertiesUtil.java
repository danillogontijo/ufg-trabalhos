package br.ufg.emc.imagehosting.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertiesUtil {
	
	static CompositeConfiguration composite = new CompositeConfiguration();
	
	public static Configuration create(String properties){
		try {
			Configuration config = new PropertiesConfiguration(properties);
			return config;
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void add(String properties){
		composite.addConfiguration(create(properties));
	}
	
	public static void add(Configuration config){
		composite.addConfiguration(config);
	}
	
	public static String getValue(String key){
		return composite.getString(key);
	}
	
	public static int getIntValue(String key){
		return composite.getInt(key, 0);
	}
	
	public static List<String> getListValue(String key){
		return Arrays.asList(composite.getStringArray(key));
	}

}
