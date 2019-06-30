package com.bavelsoft.schmoperties.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.io.Writer;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.util.Map;
import java.util.Set;
import com.bavelsoft.schmoperties.storenonmeta.Store;
import com.bavelsoft.schmoperties.storenonmeta.StoreFactory;
import com.bavelsoft.schmoperties.TypeMapFactory;

public abstract class AbstractModuleGenerator {
	private Filer filer;

	public AbstractModuleGenerator(Filer filer) {
		this.filer = filer;
	}

	public void write(String packageName, String configurationModule, Map<String, Class<?>> fields, Set<String> optionalFields) {
		try {
			final String fullyQualifiedName = packageName + '.' + configurationModule;
			final JavaFileObject jfo = filer.createSourceFile(fullyQualifiedName);

			try (final Writer writer = jfo.openWriter() ) {
				generate(writer, packageName, configurationModule, fields, optionalFields);
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void generate(Writer writer, String packageName, String configurationModule, Map<String, Class<?>> fields, Set<String> optionalFields)
			throws IOException, ClassNotFoundException {
		TypeSpec.Builder cls = createClass(configurationModule);

		for (String field : fields.keySet()) {
			cls.addMethod(createMethod(field, fields.get(field), optionalFields.contains(field)).build());
		}
		JavaFile javaFile = JavaFile.builder(packageName, cls.build()).build();
		javaFile.writeTo(writer);
		return;
	}

	private TypeSpec.Builder createClass(String className) {
		TypeSpec.Builder cls = TypeSpec.classBuilder(className)
			.addJavadoc("This class was automatically generated at build time\nby ConfiguredProcessor\n")
			.addField(FieldSpec.builder(Store.class, "store")
                        	.addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                        	.initializer("$T.get()", StoreFactory.class)
				.build())
			.addModifiers(Modifier.FINAL, Modifier.PUBLIC);

		enrichClass(cls);

		return cls;
	}

	protected abstract void enrichClass(TypeSpec.Builder cls);

	protected abstract Class<?> getMethodAnnotation();

	private  MethodSpec.Builder createMethod(String name, Class<?> type, boolean isOptional) {
		String shortName = name.replaceAll("[\\.\\$\\{}]","_");
		MethodSpec.Builder method = MethodSpec.methodBuilder(shortName)
			.addAnnotation(getMethodAnnotation())
			.addAnnotation(Singleton.class)
			.addAnnotation(AnnotationSpec.builder(Named.class).addMember("value","$S",name).build())
			.addStatement("return store.get$L($S, null, $L)", TypeMapFactory.capitalized(type), name, isOptional)
			.returns(type);
		return method;

	}
}
