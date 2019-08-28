package fu;

import org.junit.Test;
import com.bavelsoft.schmoperties.annotation.OverridableValue;
import static org.junit.Assert.assertEquals;

public class MyOverridableTest {

	@OverridableValue("myDefaultValue")
	private final String someField = MyOverridableTestOverridableSupport.someField();

	@Test
	public void testString() {
		assertEquals("myDefaultValue", someField);
	}

	@OverridableValue("1.2")
	private final double someOtherField = MyOverridableTestOverridableSupport.someOtherField();

	@Test
	public void testDouble() {
		assertEquals(1.2, someOtherField, 0);
	}

	@OverridableValue("myDefaultValue")
	private final String fuField = MyOverridableTestOverridableSupport.fuField();

	@Test
	public void testOverride() {
		assertEquals("myOverriddenValue", fuField);
	}

	@OverridableValue("999")
	private final int frobField = MyOverridableTestOverridableSupport.frobField();

	@Test
	public void testIntOverride() {
		assertEquals(456, frobField);
	}

	@OverridableValue("nope")
	private final String systemPropertyMu = MyOverridableTestOverridableSupport.systemPropertyMu();

	@Test
	public void testSystemProperty() {
		assertEquals("Bar", systemPropertyMu);
	}
}
