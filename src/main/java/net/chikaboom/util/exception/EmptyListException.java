package net.chikaboom.util.exception;

/**
 * Исключение возникает в случае, если в метод была передана пустая коллекция
 */
@Deprecated
public class EmptyListException extends Exception {

    public EmptyListException(String msg) {
        super(msg);
    }
}
