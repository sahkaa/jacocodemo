package io.sahka;

/**
 * @author Alexander Yushchenko
 * @since 2017.04.15
 */
public class SomeClass {
    public int add(int x, int y) {
        if (Configuration.isEnabled()) {
            return x + y;
        }
        return 0;
    }
}
