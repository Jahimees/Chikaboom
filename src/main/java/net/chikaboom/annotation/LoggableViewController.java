package net.chikaboom.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация используется для пометки методов в контроллерах, которые возвращают представление.
 * Аннотация добавляет логирование непосредственно перед самим вызовом метода контроллера и сообщает о том, что процесс
 * открытия страницы начался
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggableViewController {

}


