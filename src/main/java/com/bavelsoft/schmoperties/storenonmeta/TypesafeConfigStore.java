package com.bavelsoft.schmoperties.storenonmeta;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.text.StrSubstitutor.replaceSystemProperties;

public class TypesafeConfigStore implements Store {
	private Config config;
	private Set<String> logged = new HashSet<>();
	final Logger log = LoggerFactory.getLogger(PropertiesStore.class);

	public TypesafeConfigStore(String configFile) {
		config = ConfigFactory.parseFile(new File(configFile));
	}

	public Collection<String> getKeys() {
		Collection<String> keys = new ArrayList<String>();
		for (Map.Entry<String, ?> e : config.entrySet()) {
			keys.add(e.getKey());
		}
		return keys;
	}

	public Boolean getBoolean(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		Boolean value;
		if (config.hasPath(key))
			value = config.getBoolean(key);
		else if (defaultValue != null)
			value = Boolean.valueOf(defaultValue);
		else if (isOptional)
			value = null;
		else
			throw Store.requiredAndAbsent(key);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, value);
		return value;
	}

	public Double getDouble(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		Double value;
		if (config.hasPath(key))
			value = config.getDouble(key);
		else if (defaultValue != null)
			value = Double.valueOf(defaultValue);
		else if (isOptional)
			value = null;
		else
			throw Store.requiredAndAbsent(key);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, value);
		return value;
	}

	public Double getFloat(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		Double value;
		if (config.hasPath(key))
			value = config.getDouble(key);
		else if (defaultValue != null)
			value = Double.valueOf(defaultValue);
		else if (isOptional)
			value = null;
		else
			throw Store.requiredAndAbsent(key);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, value);
		return value;
	}

	public Long getLong(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		Long value;
		if (config.hasPath(key))
			value = config.getLong(key);
		else if (defaultValue != null)
			value = Long.valueOf(defaultValue);
		else if (isOptional)
			value = null;
		else
			throw Store.requiredAndAbsent(key);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, value);
		return value;
	}

	public Integer getInt(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		Integer value;
		if (config.hasPath(key))
			value = config.getInt(key);
		else if (defaultValue != null)
			value = Integer.valueOf(defaultValue);
		else if (isOptional)
			value = null;
		else
			throw Store.requiredAndAbsent(key);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, value);
		return value;
	}

	public Integer getShort(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		Integer value;
		if (config.hasPath(key))
			value = config.getInt(key);
		else if (defaultValue != null)
			value = Integer.valueOf(defaultValue);
		else if (isOptional)
			value = null;
		else
			throw Store.requiredAndAbsent(key);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, value);
		return value;
	}

	public String getString(String key, String defaultValue, boolean isOptional) {
		key = replaceSystemProperties(key);
		String value;
		if (config.hasPath(key))
			value = config.getString(key);
		else if (defaultValue != null)
			value = defaultValue;
		else if (isOptional)
			value = null;
		else
			throw Store.requiredAndAbsent(key);
		if (logged.add(key))
			log.info("{} is configured to have value {}", key, value);
		return value;
	}
}
