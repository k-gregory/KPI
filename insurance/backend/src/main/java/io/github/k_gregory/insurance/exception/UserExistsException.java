package io.github.k_gregory.insurance.exception;

public class UserExistsException extends RuntimeException {
    private final String name;

    public UserExistsException(String name) {
        super("User " + name + " already exists");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
