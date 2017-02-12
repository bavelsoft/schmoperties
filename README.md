# Schmoperties
Schmoperties is two java APIs designed to make configuration simple and easy to maintain.

## Benefits of both APIs
* Configuration of the right things
* Viewing the configuration usable at compile time
* Viewing the configuration used without having to run
* Extremely simple executed code
* Natural dependency injection
* Natural unit testing
* No extra dependencies

## Fields that generally don't change in each environment
### API Example
	class MyExampleClass {
		...
	
		@OverridableValue("1.2")
		public static final double myFuField = myFuField_init();
	
		...
	}

### Benefits
* All the benefits of POJ constants (e.g. supports java's final, no extra files to maintain)
* Overrides without a rebuild

## Fields that are configured in each environment
### API Example
	@Inject @Configured @Named("MyFuBarField")
	private double myBarField;

### Benefits
* Zero overhead API, from the caller's perspective works exactly like vanilla dependency injection

## HOWTO

The general approach is that many different units of code can all refer to a single @Named piece of configuration,
but that only a single unit of code should ever refer directly to a single OverridableValue field.

Sharing of an OverridableValue, if desired, should be done using java code, and not using the configuration framework.

[Dependency injection of configuration involves loading generated wiring.](DependencyInjection.md)

Viewing configuration-to-be-used before start is done using ConfigurationPrinter.

