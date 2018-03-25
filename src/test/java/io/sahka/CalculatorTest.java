package io.sahka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


public class CalculatorTest {

    @Test
    void testAssertEquals() {
        Calculator calculator = new Calculator();
        assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
    }

    @Test
    void testAssertThrows() {
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException();
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

    @Test
    void exerciseTest() {
    }
}
