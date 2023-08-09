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

/**
 * Обработка запросов на странице аккаунта
 */
@RestController
@RequestMapping("/chikaboom/account/{idAccount}")
@RequiredArgsConstructor
public class AccountController {

    @Value("${page.account}")
    private String ACCOUNT_PAGE;
    @Value("${attr.account}")
    private String ACCOUNT;

    private final ObjectMapper objectMapper;
    private final AccountDataService accountDataService;
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Перенаправляет на страницу аккаунта
     *
     * @return путь к странице аккаунта
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    public ModelAndView openAccountPage(@PathVariable int idAccount) {
        logger.info("Loading account page for account with id " + idAccount);
        ModelAndView modelAndView = new ModelAndView(ACCOUNT_PAGE);

        Account account = accountDataService.findById(idAccount)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));;

        String result = "";
        try {
            logger.info("Trying to convert account to JSON form.");
            result = objectMapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        modelAndView.addObject(ACCOUNT, result);

        return modelAndView;
    }
}
