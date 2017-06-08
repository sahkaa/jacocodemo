package io.sahka.mockito;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

public class TheTest {

    @Test
    public void testVerify() {
        MockedClass mocked = mock(MockedClass.class);
        mocked.dummyMethod();
        verify(mocked).dummyMethod();
    }

    @Test
    public void testThen() {
        MockedClass mocked = mock(MockedClass.class);
        mocked.dummyMethod();
        then(mocked).should(times(1)).dummyMethod();
    }

    @Test
    public void testThenAndVerify() {
        MockedClass mocked = mock(MockedClass.class);
        mocked.dummyMethod();
        then(mocked).should(times(1)).dummyMethod();
        verify(mocked).dummyMethod();
    }

    @Test
    public void testThenFail() {
        MockedClass mocked = mock(MockedClass.class);
        mocked.dummyMethod();
        then(mocked).should(times(1)).dummyMethod();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockedClass mocked = mock(MockedClass.class);
        verifyNoMoreInteractions(mocked);
    }

    @Test
    public void testVerifyZeroInteractions() {
        MockedClass mocked = mock(MockedClass.class);
        verifyZeroInteractions(mocked);
    }
}
