package net.chikaboom.exception;

/**
 * Исключение возникает, если была передана неизвестная команда
 */
public class UnknownCommandException extends IllegalArgumentException {

    public UnknownCommandException(String message) {
        super(message);
    }
}
