package net.chikaboom.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.chikaboom.constant.PageConstant.MAIN_PAGE;

public class EmptyCommand implements ActionCommand {

    //то же самое. В доку класса.
    //можешь добавить типа "используется в качестве заглушки, если команда не была распознана",
    // а в доку метода "Возвращает ссылку на главную страницу"
    /**
     * Пустая команда, возвращающая ссылку на главную страницу
     * @param request
     * @param response
     * КОММЕНТАРИЙ опять-таки. Каво ретурним?
     * @return
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return MAIN_PAGE;
    }
}
