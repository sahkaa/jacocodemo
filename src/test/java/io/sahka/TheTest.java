package io.sahka;

import junit.framework.TestCase;

public class TheTest extends TestCase {

    public void testNoAssert() {
    }

    public void testOneAssert() {
        assertEquals(2, 2);
    }

    public void testAssertionFailure() {
        assertEquals(1,2);
    }

    public void testTrue() {
        assertTrue(true);
    }

    public void testUnexpectedException() {
        throw new IllegalStateException("An exception");
    }
}
