package io.sahka;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Test;

public class AssertJTest {

    @Test
    public void isTestFile() throws Exception {
        assertThat(true).isTrue();
    }
}
