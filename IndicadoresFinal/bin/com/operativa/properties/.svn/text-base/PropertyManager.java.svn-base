package com.operativa.properties;

import java.util.Properties;

public class PropertyManager {

	private static PropertyManager propertyManager = null;

	private Properties properties;

	private PropertyManager() {
		properties = new Properties();
		properties.setProperty(Parameters.DB_DRIVER.toString(),
				"net.ucanaccess.jdbc.UcanaccessDriver");
	}

	public static PropertyManager instance() {

		if (propertyManager == null) {
			propertyManager = new PropertyManager();
		}

		return propertyManager;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
}
