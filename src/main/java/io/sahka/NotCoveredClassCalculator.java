package io.sahka;

public class NotCoveredClassCalculator {

    public static int minus(int a, int b) {
        if (a > b) {
            System.out.println("a more then b");
        }

        return a + b;
    }

    public static int plus(int a, int b) {
        if (a < b) {
            System.out.println("a less then b");
        }

        return a - b;
    }
}
