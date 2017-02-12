package com.bavelsoft.schmoperties.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.io.Writer;
import javax.lang.model.element.Modifier;
import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.util.List;
import com.bavelsoft.schmoperties.storenonmeta.Store;
import com.bavelsoft.schmoperties.storenonmeta.StoreFactory;
import com.bavelsoft.schmoperties.TypeMapFactory;

public class OverridableSupportGenerator {
	public static final String SUFFIX = "OverridableSupport";
	private Filer filer;

	public OverridableSupportGenerator(Filer filer) {
		this.filer = filer;
	}

	public void write(String packageName, String className, List<String> fieldNames, List<Class<?>> fieldTypes, List<String> fieldDefaults) {
		try {
			final String fullyQualifiedClassName = packageName + '.' + className + SUFFIX;
			final JavaFileObject jfo = filer.createSourceFile(fullyQualifiedClassName);
			try (final Writer writer = jfo.openWriter()) {
				generate(writer, packageName, className, fieldNames, fieldTypes, fieldDefaults);
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void generate(Writer writer, String packageName, String className, List<String> fieldNames, List<Class<?>> fieldTypes, List<String> fieldDefaults) throws IOException, ClassNotFoundException {
		TypeSpec.Builder cls = createClass(className);
		String fullyQualifiedClassName = packageName+"."+className;

		for (int i=0; i<fieldNames.size(); i++)
			cls.addMethod(createMethod(fullyQualifiedClassName, fieldNames.get(i), fieldTypes.get(i), fieldDefaults.get(i)).build());

		JavaFile javaFile = JavaFile.builder(packageName, cls.build()).build();
		javaFile.writeTo(writer);
	}

	private TypeSpec.Builder createClass(String className) {
		TypeSpec.Builder cls = TypeSpec.classBuilder(className + SUFFIX)
			.addJavadoc("This class was automatically generated at build time\nby OverridableProcessor for the configuration of $L\n", className)
			.addField(FieldSpec.builder(Store.class, "store")
                        	.addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                        	.initializer("$T.get()", StoreFactory.class)
				.build())
			.addModifiers(Modifier.FINAL);

		return cls;
	}

	private  MethodSpec.Builder createMethod(String fullyQualifiedClassName, String fieldName, Class<?> fieldType, String fieldDefault) {
		String fullyQualifiedField = fullyQualifiedClassName + "." + fieldName;

		MethodSpec.Builder method = MethodSpec.methodBuilder(fieldName+"_init")
			.addJavadoc("This should only be called as the initializer for the $L field of $L\n", fieldName, fullyQualifiedClassName)
			.addModifiers(Modifier.STATIC)
			.addStatement("return store.get$L($S, $S)", TypeMapFactory.capitalized(fieldType), fullyQualifiedField, fieldDefault)
			.returns(fieldType);

		return method;
	}
}
