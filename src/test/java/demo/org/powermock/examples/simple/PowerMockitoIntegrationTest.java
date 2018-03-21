package demo.org.powermock.examples.simple;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "demo.org.powermock.examples.simple.*")
public class PowerMockitoIntegrationTest {

    @Test
    public void verifyNewShouldWork() throws Exception {
        CollaboratorWithFinalMethods finalMethods = mock(CollaboratorWithFinalMethods.class);
        whenNew(CollaboratorWithFinalMethods.class).withNoArguments().thenReturn(finalMethods);

        CollaboratorWithFinalMethods collaborator = new CollaboratorWithFinalMethods();
        verifyNew(CollaboratorWithFinalMethods.class).withNoArguments();

        collaborator.helloMethod();
    }

    @Test
    public void verifyStaticShouldWork() throws Exception {
        spy(CollaboratorForPartialMocking.class);
        when(CollaboratorForPartialMocking.staticMethod()).thenReturn("I am a static mock method.");

        CollaboratorForPartialMocking collaborator = new CollaboratorForPartialMocking();
        CollaboratorForPartialMocking mock = spy(collaborator);

        when(mock, "privateMethod").thenReturn("I am a private mock method.");
        mock.privateMethodCaller();
        verifyPrivate(mock).invoke("privateMethod");
    }
}
