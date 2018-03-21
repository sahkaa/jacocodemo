package demo.org.powermock.examples.simple;

import static org.powermock.api.easymock.PowerMock.verify;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor
public class EasyMockAssertionsTest {

    @Test
    public void verifyShouldWork() throws Exception {
        verify(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void expectedExceptionShouldWork() throws Exception {
        throw new IllegalArgumentException("Unexpected exeception");
    }

    @Test
    public void verifyAllShouldWork() throws Exception {
        verifyAll();
    }
}
