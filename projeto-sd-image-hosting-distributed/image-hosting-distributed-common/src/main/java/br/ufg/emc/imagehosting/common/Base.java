package br.ufg.emc.imagehosting.common;

import java.util.List;

import org.apache.commons.configuration.Configuration;

import br.ufg.emc.imagehosting.util.PropertiesUtil;

public abstract class Base {
	
	private static final Configuration config = PropertiesUtil.create("config.properties");
	
	public Base(){
		PropertiesUtil.add(config); 
	}
	
	public List<String> getListValues(String key){
		return PropertiesUtil.getListValue(key);
	}
	
	public String getValue(String key){
		return PropertiesUtil.getValue(key);
	}
	
	public int getIntValue(String key){
		return PropertiesUtil.getIntValue(key);
	}

}
