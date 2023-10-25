package net.chikaboom.controller.error;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.response.CustomResponseObject;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Перехватывает исключения отсылая их с нужной информацией на клиентскую часть.
 * <p>
 * В случае, если пользователь вводит какие-то некорректные данные, которые уходят на сервер, то
 * в процессе обработки данных, выбрасывается ошибка, которая перехватывается этим классом,
 * обрабатывается и отдает на клиентскую часть объект ошибки с содержанием http кода.
 */
@ControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Перехватывает исключение некорректно-введенных данных пользователем
     *
     * @param ex экземпляр исключения
     * @return объект ошибки с http кодом 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponseObject> illegalArgumentException(IllegalArgumentException ex) {
        logger.warn(ex.getMessage());

        return new ResponseEntity<>(new CustomResponseObject(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "unknown"
        ), HttpStatus.BAD_REQUEST);
    }

    /**
     * Перехватывает исключение неверно введенных данных авторизации
     *
     * @param ex экземпляр исключения
     * @return оюъект ошибки с кодом 403
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomResponseObject> badCredentials(BadCredentialsException ex) {
        logger.warn(ex.getMessage());

        return new ResponseEntity<>(new CustomResponseObject(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                "unknown"
        ), HttpStatus.FORBIDDEN);
    }

    /**
     * Перехватывает исключение попытки создания пользователя с уже существующими уникальными данными
     *
     * @param ex экземпляр исключения
     * @return объект ошибки с http кодом 400
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<CustomResponseObject> userAlreadyExists(UserAlreadyExistsException ex) {
        logger.warn("Trying to create user with same parameters");
        logger.warn(ex.getMessage());

        return new ResponseEntity<>(new CustomResponseObject(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "unknown"
        ), HttpStatus.BAD_REQUEST);
    }

    /**
     * Перехватывает исключение попытки обращения к несуществующим данным
     *
     * @param ex экземпляр исключения
     * @return объект ошибки с http кодом 404
     */
    @ExceptionHandler(NoSuchDataException.class)
    public ResponseEntity<CustomResponseObject> noSuchData(NoSuchDataException ex) {
        logger.error("Trying to get not existing data");
        logger.error(ex.getMessage());

        return new ResponseEntity<>(new CustomResponseObject(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                "unknown"
        ), HttpStatus.NOT_FOUND);
    }

    /**
     * Перехватывает исключение попытки обращения к данным, к которым у пользователя нет доступа
     *
     * @param ex экземпляр исключения
     * @return объект ошибки с http кодом 403
     */
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<CustomResponseObject> illegalAccess(IllegalAccessException ex) {
        logger.error(ex.getMessage());

        return new ResponseEntity<>(new CustomResponseObject(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                "unknown"
        ), HttpStatus.FORBIDDEN);
    }
}
