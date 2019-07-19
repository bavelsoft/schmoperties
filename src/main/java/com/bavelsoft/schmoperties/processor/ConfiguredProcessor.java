package com.bavelsoft.schmoperties.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.tools.Diagnostic;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.io.Writer;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import javax.tools.FileObject;
import static java.util.stream.Collectors.toSet;
import static javax.tools.StandardLocation.CLASS_OUTPUT;
import com.google.auto.service.AutoService;
import javax.inject.Named;
import com.bavelsoft.schmoperties.TypeMapFactory;
import com.bavelsoft.schmoperties.generator.DaggerModuleGenerator;
import com.bavelsoft.schmoperties.generator.GuiceModuleGenerator;
import com.bavelsoft.schmoperties.annotation.ConfigurationModule;
import com.bavelsoft.schmoperties.annotation.Configured;

@AutoService(Processor.class)
public class ConfiguredProcessor extends AbstractProcessor {
        private static Map<String, Class<?>> typeMap = TypeMapFactory.createTypeMap();
	private Messager messager;
	private Elements elementUtils;
	private DaggerModuleGenerator daggerGenerator;
	private GuiceModuleGenerator guiceGenerator;
	private String configurationModule;

	@Override
	public synchronized void init(ProcessingEnvironment env){
		super.init(env);
		messager = env.getMessager();
		elementUtils = env.getElementUtils();
		daggerGenerator = new DaggerModuleGenerator(env.getFiler());
		guiceGenerator = new GuiceModuleGenerator(env.getFiler());
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotationsParam, RoundEnvironment env) {
		try {
			return processAndThrow(annotationsParam, env);
		} catch (Exception e) {
			System.err.println(e);
			throw(e);
		}
	}

	private boolean processAndThrow(Set<? extends TypeElement> annotationsParam, RoundEnvironment env) {
		Map<String, Class<?>> fields = getFields(env);
		Set<String> optionalFields = getOptionalFields(env);
		Set<? extends Element> configurationModuleElements = env.getElementsAnnotatedWith(ConfigurationModule.class);
		complainOfMissingConfigurationModule(env, fields.keySet(), configurationModuleElements);
		complainOfInconsistentAnnotation(env, fields.keySet());
		for (Element element : configurationModuleElements)
			generateConfigurationModule(element, fields, optionalFields);
		return false;
	}

	private void generateConfigurationModule(Element element, Map<String, Class<?>> fields, Set<String> optionalFields) {
		String className = element.getAnnotation(ConfigurationModule.class).value();
		String packageName = elementUtils.getPackageOf(element).toString();

		//only guice employs a configure() method, i.e. ExecutableElement
		if (element instanceof ExecutableElement || isGuiceModule(element))
			guiceGenerator.write(packageName, className, fields, optionalFields);
		else
			daggerGenerator.write(packageName, className, fields, optionalFields);
	}

	private boolean isGuiceModule(Element element) {
		if (!(element instanceof TypeElement))
			return false;
		return ((TypeElement)element).getSuperclass().toString().equals("com.google.inject.AbstractModule");
	}

	private Map<String, Class<?>> getFields(RoundEnvironment env) {
		Map<String, Class<?>> fields = new HashMap<>();
		for (Element element : env.getElementsAnnotatedWith(Configured.class)) {
			if (element.getAnnotation(Named.class) == null) {
				System.err.println(element.toString()+" is not Named");
				continue;
			}
			String name = element.getAnnotation(Named.class).value();
			fields.put(name, typeMap.get(element.asType().toString()));
		}
		return fields;
	}

	private Set<String> getOptionalFields(RoundEnvironment env) {
		Set<String> fields = new HashSet<>();
		for (Element element : env.getElementsAnnotatedWith(Configured.class))
			for (AnnotationMirror mirror : element.getAnnotationMirrors())
				if (mirror.getAnnotationType().asElement().getSimpleName().contentEquals("Nullable"))
					fields.add(element.getAnnotation(Named.class).value());
		return fields;
	}

	private void complainOfMissingConfigurationModule(RoundEnvironment env,
			Set<String> configured,
			Set<? extends Element> configuredModules) {
		if (!configured.isEmpty() && configuredModules.isEmpty())
			messager.printMessage(Diagnostic.Kind.ERROR,
			      "need @ConfiguredModule in the same compilation unit, e.g. maven module",
			      env.getElementsAnnotatedWith(Configured.class).iterator().next());
	}

	private void complainOfInconsistentAnnotation(RoundEnvironment env, Set<String> configuredNames) {
		for (Element element : env.getElementsAnnotatedWith(Named.class)) {
			String name = element.getAnnotation(Named.class).value();
			if (configuredNames.contains(name) && element.getAnnotation(Configured.class) == null)
				messager.printMessage(Diagnostic.Kind.ERROR,
					"`"+element.toString()+"' needs to be annotated @Configured,"+
					" because @Named(\""+name+"\") is annotated @Configured elsewhere",
					element);
		}
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return annotationTypesForClasses(Named.class, ConfigurationModule.class);
	}

	private Set<String> annotationTypesForClasses(Class<?>... classes) {
		return Arrays.stream(classes).map(c->c.getCanonicalName()).collect(toSet());
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
}
