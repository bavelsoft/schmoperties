# Schmoperties
Schmoperties is two java APIs designed to make configuration simple and easy to maintain.

## Benefits
* Viewing the configuration usable at compile time
* Viewing the configuration used without having to run
* Natural dependency injection
* Natural unit testing
* No extra dependencies

## Fields that are configured in each environment
### Example
	@Configured @Inject @Named("MyFuBarField")
	private double myBarField;

The myBarField field will be injected just like with regular dependency injection,
except that its value is coming from the configuration file,
and not from a dependency provider in code.

## Fields that generally don't change in each environment
### Example
	class MyExampleClass {
		...
	
		@OverridableValue("1.2")
		public static final double myFuField = getDouble("myFuField");
	
		...
	}

The myFuField will be assigned the value 1.2 by default,
but if somebody specifies a different value for MyExampleClass.myFuField in the configuration file,
it will be used instead.

## HOWTO

By default the configuration file is environment.conf. If "typesafe config" is present on the classpath,
then environment.conf can be in the hocon format. Otherwise it's just java properties.

The general approach is that many different units of code can all refer to a single @Named piece of configuration,
but that only a single unit of code should ever refer directly to a single OverridableValue field.

Sharing of an OverridableValue, if desired, should be done using java code, and not using the configuration framework.

[Dependency injection of configuration involves loading generated wiring.](DependencyInjection.md)

Viewing configuration-to-be-used before start is done using ConfigurationPrinter.

