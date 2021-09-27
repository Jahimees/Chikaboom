package net.chikaboom.exception;

public class UnknownCommandException extends IllegalArgumentException {

    public UnknownCommandException(String message) {
        super(message);
    }
}
