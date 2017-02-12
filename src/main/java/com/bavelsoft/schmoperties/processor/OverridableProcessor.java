package com.bavelsoft.schmoperties.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.tools.Diagnostic;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.Writer;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import javax.tools.FileObject;
import static javax.tools.StandardLocation.CLASS_OUTPUT;
import com.google.auto.service.AutoService;
import com.bavelsoft.schmoperties.TypeMapFactory;
import com.bavelsoft.schmoperties.generator.OverridableSupportGenerator;
import com.bavelsoft.schmoperties.annotation.OverridableValue;

@AutoService(Processor.class)
public class OverridableProcessor extends AbstractProcessor {
	private static Map<String, Class<?>> typeMap = TypeMapFactory.createTypeMap();
	private Messager messager;
	private Elements elementUtils;
	private OverridableSupportGenerator generator;

	@Override
	public synchronized void init(ProcessingEnvironment env){
System.err.println("running OverridableProcessor");
		super.init(env);
		messager = env.getMessager();
		elementUtils = env.getElementUtils();
		generator = new OverridableSupportGenerator(env.getFiler());
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotationsParam, RoundEnvironment env) {
		Map<TypeElement, Collection<Element>> classes = new HashMap<>();
		for (Element annotatedElement : env.getElementsAnnotatedWith(OverridableValue.class)) {
			TypeElement cls = (TypeElement)annotatedElement.getEnclosingElement();
System.err.println("processing "+cls);
			if (!classes.containsKey(cls))
				classes.put(cls, new ArrayList<>());
			classes.get(cls).add(annotatedElement);
		}
		for (TypeElement cls : classes.keySet())
			generateClass(cls, classes.get(cls));
		return true;
	}

	private void generateClass(TypeElement cls, Collection<Element> fields) {
		String packageName = elementUtils.getPackageOf(cls).toString();
		String className = cls.getSimpleName().toString();
		List<String> fieldNames = new ArrayList<>();
		List<Class<?>> fieldTypes = new ArrayList<>();
		List<String> fieldDefaults = new ArrayList<>();
		for (Element field : fields) {
			if (field.getAnnotation(OverridableValue.class) == null)
				continue;
			Class<?> fieldType = typeMap.get(field.asType().toString());
			String fieldDefault = field.getAnnotation(OverridableValue.class).value();
			checkDefault(field, fieldDefault);
			fieldNames.add(field.getSimpleName().toString());
			fieldTypes.add(fieldType);
			fieldDefaults.add(fieldDefault);
		}
		generator.write(packageName, className, fieldNames, fieldTypes, fieldDefaults);
	}

	private void checkDefault(Element field, String fieldDefault) {
		String fieldTypeName = field.asType().toString();
		if (!fieldTypeName.equals("java.lang.String")) {
			String wrapperName = "java.lang." + fieldTypeName.substring(0, 1).toUpperCase() + fieldTypeName.substring(1);
			if (wrapperName.equals("java.lang.Int"))
				wrapperName = "java.lang.Integer";
			try {
				Class.forName(wrapperName).getMethod("valueOf", String.class).invoke(null, fieldDefault);
			} catch (Exception e) {
				messager.printMessage(Diagnostic.Kind.ERROR, fieldDefault+" is not a valid @OverridableValue value", field);
			}
		}
	}
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.singleton(OverridableValue.class.getCanonicalName());
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
}
