package net.chikaboom.exception;

/**
 * Исключение возникает, когда пользователь пытается получить доступ к данным, к которым у него нет достаточно прав
 */
public class IllegalAccessException extends Exception {

    public IllegalAccessException(String msg) {
        super(msg);
    }
}
