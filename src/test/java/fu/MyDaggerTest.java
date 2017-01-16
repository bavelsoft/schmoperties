package fu;

import org.junit.Test;
import org.junit.Ignore;
import schmoperties.annotation.Configured;
import schmoperties.annotation.ConfigurationModule;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Component;

import static org.junit.Assert.assertEquals;

public class MyDaggerTest {

	static class WiredClass {
		@Inject @Configured
		@Named("fubar") String fuBar;

		@Inject @Configured
		@Named("Qux") int qux;

		@Inject
		WiredClass() {}
	}

	@Singleton
	@Component(
		modules=MyDaggerConfigModule.class
	)
	@ConfigurationModule("MyDaggerConfigModule")
	interface MyComponent {
		WiredClass wiredClass();
	}

	@Test
	public void testWiredField() {
		MyComponent m = DaggerMyDaggerTest_MyComponent.create();
		WiredClass w = m.wiredClass();
		assertEquals("baz", w.fuBar);
	}

	@Test
	public void testWiredInt() {
		MyComponent m = DaggerMyDaggerTest_MyComponent.create();
		WiredClass w = m.wiredClass();
		assertEquals(123, w.qux);
	}
}
