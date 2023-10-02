package net.chikaboom.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Аспект, отвечающий за логирование по всему проекту
 */
@Aspect
@Component
public class LoggerAspect {

    /**
     * Внутренний класс для удобства пользования в методах-advice и для избежания дублирования кода
     */
    static class SignatureUtils {
        private MethodSignature methodSignature;
        private String className;
        private String methodName;
        private Logger logger;
        private StringBuilder operationName;
    }

    /**
     * Pointcut определяет вызовы всех методов из всех классов DataService
     */
    @Pointcut("execution(* net.chikaboom.service.data.*DataService.*(..))")
    public void dataServiceMethods() {
    }

    @Pointcut("execution(* net.chikaboom.controller.rest.*RestController.*(..))")
    public void restMethods() {
    }

    @Around("restMethods()")
    public Object logRestOperation(ProceedingJoinPoint joinPoint) {
        SignatureUtils su = getSignatureUtils(joinPoint);
        String targetClassName = su.className.split("RestController")[0];
        su.logger.info(su.methodName + " (rest operation): " + su.operationName.toString().toUpperCase() + " "
                + targetClassName + " ");
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            su.logger.error(su.methodName + " (rest operation): cannot " + su.operationName.toString().toUpperCase() +
                    " " + targetClassName, e);
            throw new RuntimeException(e);
        }

        su.logger.info(su.methodName + " (rest operation): operation '" + su.operationName + " " + targetClassName + "' was successfully done");
        return proceed;
    }

    /**
     * Производит логирование методов класса DataService. Выводит информацию о начале работы метода, возникновении ошибки,
     * а также об успешном завершении метода
     *
     * @param joinPoint информация точки вхождения
     * @return возвращаемое значение "истинного" объекта, а не прокси
     */
    @Around("dataServiceMethods()")
    public Object logDataFindOperation(ProceedingJoinPoint joinPoint) {
        SignatureUtils su = getSignatureUtils(joinPoint);
        String targetClassName = su.className.split("DataService")[0];

        su.logger.info(su.methodName + ": " + su.operationName.toString().toUpperCase() +
                " " + targetClassName + " info with arguments " + Arrays.toString(joinPoint.getArgs()));
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable t) {
            su.logger.error(su.methodName + ": cannot " + su.operationName.toString().toUpperCase() +
                    " " + targetClassName + " info with arguments " + Arrays.toString(joinPoint.getArgs()), t);
            throw new RuntimeException(t);
        }

        su.logger.info(su.methodName + ": information about " + targetClassName +
                " was " + su.operationName.toString().toUpperCase());
        return proceed;
    }

    /**
     * Выводит информацию о начале открытия страницы/вкладки
     *
     * @param joinPoint информация точки вхождения
     * @return возвращаемое значение "истинного" объекта, а не прокси
     */
    @Around("@annotation(net.chikaboom.annotation.LoggableViewController)")
    public Object logViewOpening(ProceedingJoinPoint joinPoint) {
        SignatureUtils su = getSignatureUtils(joinPoint);

        su.logger.info(su.methodSignature.getName() + ": opening...");
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable t) {
            su.logger.error(su.methodName + ": cannot open!", t);
            throw new RuntimeException(t);
        }
        return proceed;
    }

    /**
     * Утилитный метод для раскрытия joinPoint в {@link SignatureUtils} для удобного пользования
     *
     * @param joinPoint информация точки вхождения
     * @return собранный объект {@link SignatureUtils} с разобранной информацией из joinPoint
     */
    private SignatureUtils getSignatureUtils(ProceedingJoinPoint joinPoint) {
        SignatureUtils signatureUtils = new SignatureUtils();
        signatureUtils.methodSignature = (MethodSignature) joinPoint.getSignature();
        signatureUtils.className = signatureUtils.methodSignature.getDeclaringType().getSimpleName();
        signatureUtils.methodName = signatureUtils.methodSignature.getName();
        signatureUtils.logger = Logger.getLogger(signatureUtils.className);
        signatureUtils.operationName = new StringBuilder();
        for (char ch : signatureUtils.methodName.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                break;
            }

            signatureUtils.operationName.append(ch);
        }

        return signatureUtils;
    }
}
