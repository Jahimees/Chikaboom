package net.chikaboom.controller.tab;

import net.chikaboom.annotation.LoggableViewController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Отвечает за отрисовку вкладки клиентов
 */
@Controller
@RequestMapping("/chikaboom/personality/{idAccount}/clients")
public class ClientTabController {

    @Value("${tab.client}")
    private String clientTabPage;

    /**
     * Открывает вкладку клиентов на странице личного кабинета
     *
     * @param idAccount идентификатор аккаунта
     * @return путь к отрисовываемой странице
     */
    @GetMapping
    @LoggableViewController
    public String openClientTab(@PathVariable int idAccount) {
        return clientTabPage;
    }
}
