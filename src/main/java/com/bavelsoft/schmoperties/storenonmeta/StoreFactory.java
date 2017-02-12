package com.bavelsoft.schmoperties.storenonmeta;

public class StoreFactory {
	private static final Store store = create();

	private static Store create() {
		String configFile = "config/environment.conf";
		try {
			return new TypesafeConfigStore(configFile);
		} catch (NoClassDefFoundError e) {
			return new PropertiesStore(configFile);
		}
	}

	public static Store get() {
		return store;
	}
}
