package io.sahka;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }
}
