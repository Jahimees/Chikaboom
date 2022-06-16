package net.chikaboom.exception;

/**
 * Исключение возникает при попытке создать пользователя с уникальными атрибутами, которые уже зарегистрированы в системе
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
