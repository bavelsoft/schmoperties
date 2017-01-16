package schmoperties.storenonmeta;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesStore implements Store {
	private Properties properties;

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
		String stringValue = properties.getProperty(key, defaultValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Boolean.valueOf(stringValue);
	}

	public double getDouble(String key, String defaultValue) {
		String stringValue = properties.getProperty(key, defaultValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Double.valueOf(stringValue);
	}

	public double getFloat(String key, String defaultValue) {
		String stringValue = properties.getProperty(key, defaultValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Double.valueOf(stringValue);
	}

	public long getLong(String key, String defaultValue) {
		String stringValue = properties.getProperty(key, defaultValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Long.valueOf(stringValue);
	}

	public int getInt(String key, String defaultValue) {
		String stringValue = properties.getProperty(key, defaultValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Integer.valueOf(stringValue);
	}

	public int getShort(String key, String defaultValue) {
		String stringValue = properties.getProperty(key, defaultValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return Integer.valueOf(stringValue);
	}

	public String getString(String key, String defaultValue) {
		String stringValue = properties.getProperty(key, defaultValue);
		if (stringValue == null)
			throw Store.requiredAndAbsent(key);
		else
			return properties.getProperty(stringValue);
	}
}
