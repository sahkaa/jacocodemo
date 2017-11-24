package io.sahka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;


public class CalculatorTest {

    @Test
    void testAssertEquals() {
        Calculator calculator = new Calculator();
        assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
    }

    @Test
    void testAssertThrows() {
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                throw new RuntimeException();
            }
        });
    }

    @Test
    void testAssertTrue() {
        assertTrue(true);
    }

    @Test
    void testAssertFalse() {
        assertFalse(Boolean.TRUE.equals(false));
    }

    @Test
    void testAssertNull() {
        assertNull(null);
    }

    @Test
    void testAssertNotNull() {
        assertNotNull("notNull");
    }
}
