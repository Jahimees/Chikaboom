package net.chikaboom.controller;

import net.chikaboom.annotation.LoggableViewController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Подготавливает и загружает страницу мастера
 */
@Controller
@RequestMapping("/chikaboom/personality/{idAccount}")
public class PersonalityController {

    @Value("${page.personality}")
    private String PERSONALITY_PAGE;


    /**
     * Производит подготовку данных и открытие страницы личного кабинета мастера. Также проверяет аутентификацию пользователя
     *
     * @param idAccount идентификатор пользователя
     * @return представление личного кабинета, а также json аккаунта пользователя
     */
    @PreAuthorize("hasAnyRole('MASTER', 'CLIENT') and #idAccount == authentication.principal.idAccount")
    @GetMapping
    @LoggableViewController
    public String openPersonalityPage(@PathVariable int idAccount) {
        return PERSONALITY_PAGE;
    }
}
