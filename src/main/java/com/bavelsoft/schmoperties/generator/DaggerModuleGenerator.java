package com.bavelsoft.schmoperties.generator;

import com.squareup.javapoet.TypeSpec;
import javax.annotation.processing.Filer;
import dagger.Module;
import dagger.Provides;

public class DaggerModuleGenerator extends AbstractModuleGenerator {
	public DaggerModuleGenerator(Filer filer) {
		super(filer);
	}

	protected void enrichClass(TypeSpec.Builder cls) {
		cls.addAnnotation(Module.class);
	}

	protected Class<?> getMethodAnnotation() {
		return Provides.class;
	}
}
