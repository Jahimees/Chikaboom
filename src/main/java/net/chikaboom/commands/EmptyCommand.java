package net.chikaboom.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.chikaboom.constant.PageConstant.MAIN_PAGE;

public class EmptyCommand implements ActionCommand {

    /**
     * Пустая команда, возвращающая ссылку на главную страницу
     * @param request
     * @param response
     * @return
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return MAIN_PAGE;
    }
}
