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
	private Logger log = LoggerFactory.getLogger(PropertiesStore.class);

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

	public Boolean getBoolean(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null && !isOptional)
			throw Store.requiredAndAbsent(key);
		else if (stringValue == null)
			return null;
		else
			return Boolean.valueOf(stringValue);
	}

	public Double getDouble(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null && !isOptional)
			throw Store.requiredAndAbsent(key);
		else if (stringValue == null)
			return null;
		else
			return Double.valueOf(stringValue);
	}

	public Double getFloat(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null && !isOptional)
			throw Store.requiredAndAbsent(key);
		else if (stringValue == null)
			return null;
		else
			return Double.valueOf(stringValue);
	}

	public Long getLong(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null && !isOptional)
			throw Store.requiredAndAbsent(key);
		else if (stringValue == null)
			return null;
		else
			return Long.valueOf(stringValue);
	}

	public Integer getInt(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null && !isOptional)
			throw Store.requiredAndAbsent(key);
		else if (stringValue == null)
			return null;
		else
			return Integer.valueOf(stringValue);
	}

	public Integer getShort(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null && !isOptional)
			throw Store.requiredAndAbsent(key);
		else if (stringValue == null)
			return null;
		else
			return Integer.valueOf(stringValue);
	}

	public String getString(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		String stringValue = properties.getProperty(key, defaultValue);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, stringValue);
		if (stringValue == null && !isOptional)
			throw Store.requiredAndAbsent(key);
		else
			return properties.getProperty(stringValue);
	}
}
