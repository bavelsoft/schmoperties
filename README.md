# Schmoperties
Schmoperties is two java APIs designed to make configuration simple and easy to maintain:
Per Environment and Overridable Constants.

## Benefits
* Ability to view what is configurable in your code, at compile time
* Ability to view how your code is configured, without having to execute it
* Natural dependency injection
* Natural unit testing
* No extra dependencies

## Per Environment

This is for fields that are explicitly configured for each environment.

	@Named("MyFooBarField") @Inject @Configured
	private double myBarField;

The `myBarField` field will be injected just like with regular dependency injection,
except that its value comes from the `myFooBarField` entry in the configuration file,
and not from a dependency provider in your code.
`@Named` and `@Inject` are the standard dependency injection annotations from JSR 330,
and `@Configured` just causes schmoperties to generate vanilla binding code for the named injection.

Currently Guice and Dagger2 are supported.
See the link below for instructions to load the generated bindings.

## Overridable Constants

This is for fields that generally don't change in each environment.

	package mypackage;
	...
	
	class MyExampleClass {
		...
	
		@OverridableValue("1.2")
		public static final double myFuField = myFuField();
	
		...
	}

The `myFuField` will be assigned the value 1.2 by default,
but if somebody specifies a different value for `mypackage.MyExampleClass.myFuField` in the configuration file,
it will be used instead.
`myFuField()` is the statically imported schmoperties call.
This little bit of boilerplate enables avoiding magic other than build-time code generation,
and even enables the overridable variables to be java "final".

This API is designed to be used in place of regular compile time constants.
It allows those "constants" to be overridable at run time, without rebuilding or redeploying.
Except for an extra dollop of complexity, it has all of the advantages of regular constants.
Since this kind of configuration creates a tight coupling to the source code,
it should only be overridden for short periods of time,
e.g. until the next release of the application.

## HOWTO

By default the configuration file is environment.conf. If "typesafe config" is present on the classpath,
then environment.conf can be in the hocon format. Otherwise it's just java properties.

The general approach is that many different units of code can all refer to a single @Named piece of configuration,
but that only a single unit of code should ever refer directly to a single @OverridableValue field.
Sharing of an @OverridableValue should only be done using java code, and not using the configuration framework.

### [Dependency injection of configuration requires loading generated wiring.](doc/DependencyInjection.md)

### [Different configurations can reside in a single configuration file.](doc/Multiple.md)

### Viewing configuration used

Viewing configuration-to-be-used before start is done using the ConfigurationPrinter class,
see the pom.xml file for an example.
