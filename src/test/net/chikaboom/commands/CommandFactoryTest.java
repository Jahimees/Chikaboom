package net.chikaboom.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;

class CommandFactoryTest {

    @Test
    void checkDefineCommand() {
        /*Не получится без mock...
        Потому что принимается HttpServletRequest
        Поэтому надо создавать, как я понимаю, псевдоподключение, или, как минимум, псевдоREQUEST с методом POST
        Но общий смысл вспомнил, должно быть что-то вроде такого (Фантазер!!!):

        CommandFactory factory = new CommandFactory(); - должно заноситься в @BeforeAll,
                                                                                    если будем проверять все команды

        Создать_нечто,_из_чего_можно_извлечь_запрос() {
            скорее всего куча
            @Override
            @Override
            @Override
            "http://chikaboom.com/view/main.jsp?command=null" тоже где-то скорее всего будет фигурировать
        }

        И собственно суть:

        String expected = "view/main.jsp";// или MAIN_PAGE; как именно будет правильно не уверен
        String actual = factory.defineCommand(request, response);

        Assertions.assertEquals(expected, actual);
        */
    }
}