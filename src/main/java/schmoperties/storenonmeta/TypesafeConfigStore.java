package schmoperties.storenonmeta;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class TypesafeConfigStore implements Store {
	private Config config;

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

	public boolean getBoolean(String key, String defaultValue) {
		if (config.hasPath(key))
			return config.getBoolean(key);
		else if (defaultValue != null)
			return Boolean.valueOf(defaultValue);
		else
			throw Store.requiredAndAbsent(key);
	}

	public double getDouble(String key, String defaultValue) {
		if (config.hasPath(key))
			return config.getDouble(key);
		else if (defaultValue != null)
			return Double.valueOf(defaultValue);
		else
			throw Store.requiredAndAbsent(key);
	}

	public double getFloat(String key, String defaultValue) {
		if (config.hasPath(key))
			return config.getDouble(key);
		else if (defaultValue != null)
			return Double.valueOf(defaultValue);
		else
			throw Store.requiredAndAbsent(key);
	}

	public long getLong(String key, String defaultValue) {
		if (config.hasPath(key))
			return config.getLong(key);
		else if (defaultValue != null)
			return Long.valueOf(defaultValue);
		else
			throw Store.requiredAndAbsent(key);
	}

	public int getInt(String key, String defaultValue) {
		if (config.hasPath(key))
			return config.getInt(key);
		else if (defaultValue != null)
			return Integer.valueOf(defaultValue);
		else
			throw Store.requiredAndAbsent(key);
	}

	public int getShort(String key, String defaultValue) {
		if (config.hasPath(key))
			return config.getInt(key);
		else if (defaultValue != null)
			return Integer.valueOf(defaultValue);
		else
			throw Store.requiredAndAbsent(key);
	}

	public String getString(String key, String defaultValue) {
		if (config.hasPath(key))
			return config.getString(key);
		else if (defaultValue != null)
			return defaultValue;
		else
			throw Store.requiredAndAbsent(key);
	}
}
