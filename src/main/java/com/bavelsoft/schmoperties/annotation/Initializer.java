package com.bavelsoft.schmoperties.annotation;

import static com.bavelsoft.schmoperties.generator.OverridableSupportGenerator.CLASS_SUFFIX;

/*
 * This class is for people who don't want to introduce a compile time depedency on generated code.
 *
 * So instead of:
 * 	@OverridableValue("1.2") double myField = myField();
 *
 * you can use:
 *	@OverridableValue("1.2") double myField = getDouble("myField");
 *
 * The tradeoff is that this API does not check that the field name is correct at compile time.
 *
 */
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
			return c.getMethod(fieldName).invoke(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

