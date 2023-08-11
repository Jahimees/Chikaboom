package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.service.data.AccountDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;


/**
 * Подготавливает и загружает страницу мастера
 */
@RestController
@RequestMapping("/chikaboom/personality/{idAccount}")
@RequiredArgsConstructor
public class PersonalityController {

    @Value("${attr.account}")
    private String ACCOUNT;

    private final AccountDataService accountDataService;
    private final ObjectMapper objectMapper;
    private final Logger logger = Logger.getLogger(this.getClass());

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
    public ModelAndView openPersonalityPage(@PathVariable int idAccount) {
        logger.info("Opening personality page...");

        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            logger.warn("Account (id=" + idAccount + ") does not exist");
            throw new NoSuchDataException("Account (id=" + idAccount + ") does not exist");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PERSONALITY_PAGE);

        try {

            logger.info("Trying to convert account data to JSON format.");
            String accountJSON = objectMapper.writeValueAsString(accountOptional.get());
            modelAndView.addObject(ACCOUNT, accountJSON);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        return modelAndView;
    }
}
