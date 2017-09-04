package fu;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import com.bavelsoft.schmoperties.annotation.Configured;
import com.bavelsoft.schmoperties.annotation.ConfigurationModule;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

public class MyGuiceTest {

	static class WiredClass {
		@Inject @Configured @Named("fubar") String fuBar;
		@Inject @Configured @Named("${var}bar") String varBar;
		@Inject @Configured @Named("Qux") int qux;

		@Inject
		WiredClass() {}
	}

        static class MyModule extends AbstractModule {
        	@ConfigurationModule("MyGuiceConfigModule")
		protected void configure() {
			install(new MyGuiceConfigModule());
		}
        }

	@Test
	public void testWiredField() {
		WiredClass w = Guice.createInjector(new MyModule()).getInstance(WiredClass.class);
		assertEquals("baz", w.fuBar);
	}

	@Test
	public void testVarField() {
		System.getProperties().setProperty("var","fu");
		WiredClass w = Guice.createInjector(new MyModule()).getInstance(WiredClass.class);
		assertEquals("baz", w.varBar);
	}

	@Test
	public void testWiredInt() {
		WiredClass w = Guice.createInjector(new MyModule()).getInstance(WiredClass.class);
		assertEquals(123, w.qux);
	}
}
