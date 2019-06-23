package com.bavelsoft.schmoperties.storenonmeta;

import java.util.Collection;

public interface Store {
	Collection<String> getKeys();

	Boolean getBoolean(String key, String defaultValue, boolean isOptional);
	Double getDouble(String key, String defaultValue, boolean isOptional);
	Double getFloat(String key, String defaultValue, boolean isOptional);
	Long getLong(String key, String defaultValue, boolean isOptional);
	Integer getInt(String key, String defaultValue, boolean isOptional);
	default Integer getInteger(String key, String defaultValue, boolean isOptional) { return getInt(key, defaultValue, isOptional); }
	Integer getShort(String key, String defaultValue, boolean isOptional);
	String getString(String key, String defaultValue, boolean isOptional);

	//Use an error instead of an exception to indicate that we don't expect it to be caught; Guice discourages throwing exceptions from provider methods.
	static Error requiredAndAbsent(String key) { return new Error("configuration field "+key+" must be specified in the configuration file!"); }
}
