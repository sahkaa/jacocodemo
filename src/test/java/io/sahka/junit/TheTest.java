package io.sahka.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TheTest {

    @Test
    public void testNoAssert() {
    }

    @Test
    public void testOneAssert() {
        assertEquals(2, 2);
    }

    @Test
    public void testAssertwionFailure() {
        assertEquals(1, 2);
    }

    @Test
    public void testFail() {
        fail("Hi there");
    }

    @Test
    public void testUnexpectedException() {
        throw new IllegalStateException("An exception");
    }

    @Test(expected = RuntimeException.class)
    public void testExpectedException() {
        throw new IllegalArgumentException();
    }
}
