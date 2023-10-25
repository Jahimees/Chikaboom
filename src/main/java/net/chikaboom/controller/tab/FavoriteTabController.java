package net.chikaboom.controller.tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Отвечает за отрисовку вкладки избранного
 */
@Controller
public class FavoriteTabController {

    @Value("${tab.favorite}")
    private String FAVORITE_TAB;

    /**
     * Возвращает путь к странице избранного
     * @param idAccount идентификатор аккаунта
     * @return путь к странице избранного
     */
    @GetMapping("/chikaboom/personality/{idAccount}/favorite")
    public String loadFavoriteTab(@PathVariable int idAccount) {
        return FAVORITE_TAB;
    }
}
