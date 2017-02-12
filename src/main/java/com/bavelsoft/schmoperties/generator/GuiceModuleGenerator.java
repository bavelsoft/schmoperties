package com.bavelsoft.schmoperties.generator;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Modifier;
import javax.annotation.processing.Filer;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class GuiceModuleGenerator extends AbstractModuleGenerator {
	public GuiceModuleGenerator(Filer filer) {
		super(filer);
	}

	protected void enrichClass(TypeSpec.Builder cls) {
		cls	.superclass(AbstractModule.class)
			.addMethod(MethodSpec.methodBuilder("configure")
			   .addModifiers(Modifier.PROTECTED)
			   .build());
	}

	protected Class<?> getMethodAnnotation() {
		return Provides.class;
	}
}
