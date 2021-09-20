package net.chikaboom.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionCommand {

    /**
     * Родиетльский инетерфейс для команд, выходящих из класса CommandFactory
     *
     * Используется для реализации паттерна "Фабричный метод"
     * Благодаря этому интерфейсу объекты, "выходящие" из фабрики приводятся к одному типу
     * @param request
     * @param response
     * @return
     */

    String execute(HttpServletRequest request, HttpServletResponse response);
}
