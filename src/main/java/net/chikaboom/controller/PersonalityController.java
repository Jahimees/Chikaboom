package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import net.chikaboom.exception.IllegalAccessException;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * Подготавливает и загружает страницу мастера
 */
@RestController
@RequestMapping("/chikaboom/personality/{idAccount}")
//@PreAuthorize("hasAnyRole('MASTER', 'CLIENT')")
public class PersonalityController {

    @Value("${attr.account}")
    private String ACCOUNT;

    private final AccountRepository accountRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public PersonalityController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Value("${page.personality}")
    private String PERSONALITY_PAGE;

    /**
     * Производит подготовку данных и открытие страницы личного кабинета мастера. Также проверяет аутентификацию пользователя
     *
     * @param idAccount идентификатор пользователя
     * @param request   запрос с клиента
     * @return представление личного кабинета, а также json аккаунта пользователя
     * @throws IllegalAccessException возникает в случае, если доступ пытается получить неавторизованный пользователь
     */
    @GetMapping
    public ModelAndView openPersonalityPage(@PathVariable int idAccount, HttpServletRequest request) throws IllegalAccessException {
        logger.info("Opening personality page...");

        Account requestedAccount = accountRepository.findByIdAccount(idAccount);
        Account authorizedAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        TODO FILTER! Сделать аннотацию, аспект мб. Который проверяет данные на совпадение.
        if (authorizedAccount.getIdAccount() != requestedAccount.getIdAccount()) {
            throw new IllegalAccessException("User with name " + requestedAccount.getNickname() + " trying " +
                    "to open page someone else's page!");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PERSONALITY_PAGE);


        ObjectMapper mapper = new ObjectMapper();
        String accountJSON = null;

        try {
            logger.info("Trying to convert account data to JSON format.");
            accountJSON = mapper.writeValueAsString(requestedAccount);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        modelAndView.addObject(ACCOUNT, accountJSON);

        return modelAndView;
    }
}
