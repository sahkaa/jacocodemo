package io.sahka;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CoveredClassCalculatorTest {

    @Test
    public void multiple() throws Exception {
        assertThat(CoveredClassCalculator.multiple(2, 3), is(6));
    }

    @Test
    public void divide() throws Exception {
        assertThat(CoveredClassCalculator.divide(6, 3), is(2));
    }

}
