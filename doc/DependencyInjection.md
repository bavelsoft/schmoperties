Every separately built jar that uses @Configured annotations must load its own dependency injection binding as described below.

# guice

If you already have some modue:

	class MyModule extends AbstractModule {
		public void configure() {
			bind(Something.class);
		}
	}

Change it to:

	class MyModule extends AbstractModule {
		@ConfigurationModule("MyConfigModule")
		public void configure() {
			install(new MyConfigModule());
			bind(Something.class);
		}
	}

# dagger2

If you already have some component:

	@Component(modules=Whatever.class)
	interface MyComponent {
	}

Change it to:

	@ConfigurationModule("MyConfigModule")
	@Component(modules=Whatever.class, MyConfigModule.class)
	interface MyComponent {
	}

Configuration of injected fields is required by default. If you want configuration of a field to be optional, you must annotate it as @Nullable.

	@Nullable @Configured @Named(“foo”) @Inject foo;

It doesn’t matter which package the annotation is from (e.g. javax.annotation.Nullable).

Currently, only @Named is supported, and not custom annotations.
