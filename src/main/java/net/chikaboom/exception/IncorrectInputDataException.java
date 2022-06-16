package net.chikaboom.exception;

/**
 * Исключение возникает, когда пользователь вводит неверные данные в систему и отправляет их на сервер
 */
public class IncorrectInputDataException extends RuntimeException {

    public IncorrectInputDataException(String msg) {
        super(msg);
    }
}
