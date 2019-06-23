package com.bavelsoft.schmoperties;

import java.util.Arrays;
import java.util.Map;
import static java.util.stream.Collectors.toMap;

public class TypeMapFactory { 
        public static Map<String, Class<?>> createTypeMap() {
                Map<String, Class<?>> typeMap = Arrays.stream(new Class[] {
                        boolean.class, double.class, float.class, long.class, int.class, short.class,
                        Boolean.class, Double.class, Float.class, Long.class, Integer.class, Short.class, String.class
                }).collect(toMap(c->c.isPrimitive()?c.getSimpleName():c.getName(), c->c));
                return typeMap;
        }

	public static String capitalized(Class<?> c) {
		String s = c.getSimpleName();
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
}
