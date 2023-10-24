package net.chikaboom.controller.tab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FavoriteTabController {

    @Value("${tab.favorite}")
    private String FAVORITE_TAB;

    @GetMapping("/chikaboom/personality/{idAccount}/favorite")
    public String loadFavoriteTab(@PathVariable int idAccount) {
        return FAVORITE_TAB;
    }
}
