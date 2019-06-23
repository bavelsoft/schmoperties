package com.bavelsoft.schmoperties;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import com.bavelsoft.schmoperties.annotation.Configured;
import com.bavelsoft.schmoperties.annotation.OverridableValue;
import com.bavelsoft.schmoperties.processor.ConfiguredProcessor;
import com.bavelsoft.schmoperties.processor.OverridableProcessor;
import com.bavelsoft.schmoperties.generator.OverridableSupportGenerator;
import com.bavelsoft.schmoperties.storenonmeta.Store;
import com.bavelsoft.schmoperties.storenonmeta.StoreFactory;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import javax.inject.Named;

public class ConfigurationPrinter {
	Collection<String> unusedConfigurationFields = new ArrayList<String>(), usedConfiguration = new ArrayList<String>();
	Store store = StoreFactory.get();
	Reflections reflections = new Reflections("", new FieldAnnotationsScanner());

	public ConfigurationPrinter() {
		Collection<String> usedConfigurationFields = new ArrayList<String>();
		for (Field field : getAnnotatedFields(Configured.class)) {
			String fieldName = field.getAnnotation(Named.class).value();
			usedConfigurationFields.add(fieldName);
		}
		for (Field field : getAnnotatedFields(OverridableValue.class)) {
			String fieldName = field.getDeclaringClass().getName()+"."+field.getName();
			usedConfigurationFields.add(fieldName);
		}
		for (String f : store.getKeys()) {
			if (!usedConfigurationFields.contains(f)) {
				unusedConfigurationFields.add(f);
			}
		}
		for (String f : usedConfigurationFields) {
			usedConfiguration.add(f + ": " + store.getString(f, null, true));
		}
		
	}

	Collection<Field> getAnnotatedFields(Class c) {
		return reflections.getFieldsAnnotatedWith(c);
	}

	public static void main(String[] args) {
		System.err.println("configuation summary...");
		new ConfigurationPrinter().printConfigurationSummary(s -> System.out.println(s));
	}

	public void printConfigurationSummary(Consumer<String> printer) {
		if (!unusedConfigurationFields.isEmpty()) {
			printer.accept("WARNING! The following fields are configured but not used by the application: "+unusedConfigurationFields);
		}
		for (String s : usedConfiguration) {
			printer.accept(s);
		}
	}
}
