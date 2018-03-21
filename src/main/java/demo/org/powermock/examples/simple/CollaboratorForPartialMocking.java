package demo.org.powermock.examples.simple;

class CollaboratorForPartialMocking {

    static String staticMethod() {
        return "Hello!";
    }

    final String finalMethod() {
        return "Hello!";
    }

    private String privateMethod() {
        return "Hello!";
    }

    String privateMethodCaller() {
        return privateMethod() + " Welcome to the Java world.";
    }
}
