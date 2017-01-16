package schmoperties.storenonmeta;

import java.util.Collection;

public interface Store {
	Collection<String> getKeys();

	boolean getBoolean(String key, String defaultValue);
	double getDouble(String key, String defaultValue);
	double getFloat(String key, String defaultValue);
	long getLong(String key, String defaultValue);
	int getInt(String key, String defaultValue);
	int getShort(String key, String defaultValue);
	String getString(String key, String defaultValue);

	//Use an error instead of an exception to indicate that we don't expect it to be caught; Guice discourages throwing exceptions from provider methods.
	static Error requiredAndAbsent(String key) { return new Error("configuration field "+key+" must be specified in the configuration file!"); }
}
