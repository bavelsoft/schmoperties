package com.bavelsoft.schmoperties.storenonmeta;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.text.StrSubstitutor.replaceSystemProperties;

public class PropertiesStore implements Store {
	private Properties properties;
	private Set<String> logged = new HashSet<>();
	final Logger log = LoggerFactory.getLogger(PropertiesStore.class);

	public PropertiesStore(String configFile) {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(configFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Collection<String> getKeys() {
		Collection<String> keys = new ArrayList<String>();
		Enumeration<?> e = properties.propertyNames();
		while (e.hasMoreElements()) {
			keys.add((String)e.nextElement());
		}
		return keys;
	}

	public boolean getBoolean(String key, String defaultValue) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Boolean.valueOf(stringValue);
	}

	public double getDouble(String key, String defaultValue) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Double.valueOf(stringValue);
	}

	public double getFloat(String key, String defaultValue) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Double.valueOf(stringValue);
	}

	public long getLong(String key, String defaultValue) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Long.valueOf(stringValue);
	}

	public int getInt(String key, String defaultValue) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Integer.valueOf(stringValue);
	}

	public int getShort(String key, String defaultValue) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Integer.valueOf(stringValue);
	}

	public String getString(String key, String defaultValue) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return properties.getProperty(stringValue);
	}
}
