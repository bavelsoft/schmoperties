Multiple sets of configuration, for the same @Named key, can live in the same configuration file.
Just put a system property in the @Named key,
and the appropriate set of the configuration will be selected.

For example, with configuration:

	1.hostName=fu
	2.hostName=bar

If your program is executed with:

	java -Dshard=2

Then the variable:

	@Named("${shard}.hostName") @Configured @Inject String hostname;

will be injected with the value "bar".

This should not be used for the configuration for different environments.
Each environment should get its own entirely separate edition of the configuration file,
and only the appropriate edition should be deployed to the environment.

It may be useful however for some "sub-environment" concept to have different configuration editions
in the same file,
even if a given invocation of the program only uses one of those editions.

