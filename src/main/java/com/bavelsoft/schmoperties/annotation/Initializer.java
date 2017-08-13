package com.bavelsoft.schmoperties.annotation;

import static com.bavelsoft.schmoperties.generator.OverridableSupportGenerator.CLASS_SUFFIX;
import static com.bavelsoft.schmoperties.generator.OverridableSupportGenerator.METHOD_SUFFIX;

public class Initializer {
	public static boolean getBoolean(String fieldName) { return (boolean)getFromGenerated(fieldName); }
	public static double getDouble(String fieldName) { return (double)getFromGenerated(fieldName); }
	public static float getFloat(String fieldName) { return (float)getFromGenerated(fieldName); }
	public static long getLong(String fieldName) { return (long)getFromGenerated(fieldName); }
	public static int getInt(String fieldName) { return (int)getFromGenerated(fieldName); }
	public static short getShort(String fieldName) { return (short)getFromGenerated(fieldName); }
	public static String getString(String fieldName) { return (String)getFromGenerated(fieldName); }

	private static Object getFromGenerated(String fieldName) {
		try {
			String className = new Throwable().getStackTrace()[0].getClassName() + CLASS_SUFFIX;
			Class c = Class.forName(className);
			return c.getMethod(fieldName + METHOD_SUFFIX).invoke(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

