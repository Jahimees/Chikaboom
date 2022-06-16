package net.chikaboom.controller;

import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.exception.UserAlreadyExistsException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Перехватывает исключения отсылая их с нужной информацией на клиентскую часть.
 * <p>
 * В случае, если пользователь вводит какие-то некорректные данные, которые по каким-либо причинам уходят на сервер,
 * в процессе обработки данных выбрасывается ошибка, которая перехватывается этим классом,
 * обрабатывается и отдает на клиентскую часть объект ошибки с содержанием http кода.
 */
@ControllerAdvice
public class AdviceController {

    private final Logger logger = Logger.getLogger(AdviceController.class);

    /**
     * Перехватывает исключение некорректно-введенных данных пользователем
     *
     * @param ex экземпляр исключения
     * @return объект ошибки с http кодом 400
     */
    @ExceptionHandler(IncorrectInputDataException.class)
    public ResponseEntity<Error> incorrectInputData(IncorrectInputDataException ex) {
        logger.warn("Incorrect data exception");
        logger.warn(ex.getMessage());

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Перехватывает исключение попытки создания пользователя с уже существующими уникальными данными
     *
     * @param ex экземпляр исключения
     * @return объект ошибки с http кодом 400
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Error> userAlreadyExists(UserAlreadyExistsException ex) {
        logger.warn("Trying to create user with same parameters");
        logger.warn(ex.getMessage());

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
