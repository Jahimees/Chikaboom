package net.chikaboom.controller.error;

import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.exception.UserAlreadyExistsException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Перехватывает исключения отсылая их с нужной информацией на клиентскую часть.
 * <p>
 * В случае, если пользователь вводит какие-то некорректные данные, которые по каким-либо причинам уходят на сервер,
 * в процессе обработки данных выбрасывается ошибка, которая перехватывается этим классом,
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
    @ExceptionHandler(IncorrectInputDataException.class)
    public ResponseEntity<Error> incorrectInputData(IncorrectInputDataException ex) {
        logger.warn(ex.getMessage());

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Error> badCredentials(BadCredentialsException ex) {
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

    /**
     * Перехватывает исключение попытки обращения к несуществующим данным
     *
     * @param ex экземпляр исключения
     * @return объект ошибки с http кодом 404
     */
    @ExceptionHandler(NoSuchDataException.class)
    public ResponseEntity<String> noSuchData(NoSuchDataException ex) {
        logger.error("Trying to get not existing data");
        logger.error(ex.getMessage());
        logger.info("Redirecting to 404 page...");

        URI location = null;
        try {
            location = new URI("/404");
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        return new ResponseEntity<>(responseHeaders, HttpStatus.NOT_FOUND);
    }

    /**
     * Перехватывает исключение попытки обращения к данным, к которым у пользователя нет доступа
     *
     * @param ex экземпляр исключения
     * @return объект ошибки с http кодом 403
     */
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<String> illegalAccess(IllegalAccessException ex) {
        logger.error(ex.getMessage());

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
