package io.sahka;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DevfactoryYmlTest {

    @Test
    public void multiple() throws Exception {
        assertThat(DevfactoryYmlExclude.multiple(2, 3), is(6));
    }

    @Test
    public void divide() throws Exception {
        assertThat(DevfactoryYmlExclude.divide(6, 3), is(2));
    }
}
