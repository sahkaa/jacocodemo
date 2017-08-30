package io.sahka;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MockedClass {

    private String getOrSet;

    public String dummyMethod() {
        return "dummyValue";
    }
}
