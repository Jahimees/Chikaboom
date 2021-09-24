package net.chikaboom.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Родительский инетерфейс для команд CommandFactory
 *
 * Добавляет слой абстракции для классов-команд для поддержки/возможности реализации фабрики
 */

public interface ActionCommand {
     /**
     * Метод предназначен для исполнения команды
      *
     * @return строку, содержащую путь до страницы, на которую будет перенаправляться пользователь
     */
    String execute(HttpServletRequest request, HttpServletResponse response);
}
