package net.chikaboom.exception;

/**
 * Исключение возникает в случае, если в метод была передана пустая коллекция
 */
public class EmptyListException extends Exception {

    public EmptyListException(String msg) {
        super(msg);
    }
}
