package io.sahka;

import lombok.Data;

@Data
public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }
}
