package net.chikaboom.exception;

import java.util.NoSuchElementException;

/**
 * Исключение возникает при попытке полуить доступ к несуществующим данным
 */
public class NoSuchDataException extends NoSuchElementException {

    public NoSuchDataException(String msg) {
        super(msg);
    }
}
