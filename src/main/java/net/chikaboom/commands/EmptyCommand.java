package net.chikaboom.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.chikaboom.constant.PageConstant.MAIN_PAGE;
/**
 * Пустая команда, возвращающая ссылку на главную страницу
 * Используется в качестве заглушки в случае, если полученная сервлетом команда не распознана
 */
public class EmptyCommand implements ActionCommand {
    /**
     * Пустая команда, возвращающая ссылку на главную страницу
     *
     * @return строку, содержащую ссылку на главную страницу
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return MAIN_PAGE;
    }
}
