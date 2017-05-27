package io.sahka;

public class CoveredClassCalculator {

    public static int multiple(int a, int b) {
        return a * b;
    }

    public static int divide(int a, int b) {
        return a / b;
    }

    public static int multipleNotCovered(int a, int b) {
        return a * b;
    }

    public static int divideNotCovered(int a, int b) {
        return a / b;
    }
}
