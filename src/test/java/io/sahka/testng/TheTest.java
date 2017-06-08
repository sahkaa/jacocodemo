package io.sahka.testng;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TheTest {

    @Test
    public void testNoAssert() {
    }

    @Test
    public void testOneAssert() {
    	Assert.assertEquals(2, 2);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testUnexpectedException() {
    	throw new IllegalStateException("An exception");
    }

    @Test
    public void assertNullTest() throws Exception {
        assertNull(null);
    }

    @Test
    public void assertTrueTest() throws Exception {

        assertTrue("Should have had errors",true);
    }
}
