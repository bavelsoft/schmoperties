package schmoperties.generator;

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
import schmoperties.storenonmeta.Store;
import schmoperties.storenonmeta.StoreFactory;
import schmoperties.TypeMapFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractModuleGenerator {
	//Logger logger = LoggerFactory.getLogger(ConfigurationPrinter.class); //TODO in the generated code
	
	private Filer filer;

	public AbstractModuleGenerator(Filer filer) {
		this.filer = filer;
	}

	public void write(String packageName, String configurationModule, Map<String, Class<?>> fields) {
		try {
			final String fullyQualifiedName = packageName + '.' + configurationModule;
			final JavaFileObject jfo = filer.createSourceFile(fullyQualifiedName);
			System.err.println("schmoperties created module file "+jfo.toUri());

			try (final Writer writer = jfo.openWriter() ) {
				generate(writer, packageName, configurationModule, fields);
			}
		} catch (ClassNotFoundException e) {
			;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void generate(Writer writer, String packageName, String configurationModule, Map<String, Class<?>> fields) throws IOException, ClassNotFoundException {
		TypeSpec.Builder cls = createClass(configurationModule);

		for (String field : fields.keySet())
			cls.addMethod(createMethod(field, fields.get(field)).build());
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

	private  MethodSpec.Builder createMethod(String name, Class<?> type) {
		String shortName = name.replaceAll("\\.","_");
		MethodSpec.Builder method = MethodSpec.methodBuilder(shortName)
			.addAnnotation(getMethodAnnotation())
			.addAnnotation(Singleton.class)
			.addAnnotation(AnnotationSpec.builder(Named.class).addMember("value","$S",name).build())
			.addStatement("return store.get$L($S, null)", TypeMapFactory.capitalized(type), name)
			.returns(type);
		return method;

	}
}
