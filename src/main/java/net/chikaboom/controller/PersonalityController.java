package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.exception.IllegalAccessException;
import net.chikaboom.model.database.Account;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.AccountInfoLoaderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * Подготавливает и загружает страницу мастера
 */
@RestController
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{idAccount}")
public class PersonalityController {

    @Value("${attr.account}")
    private String ACCOUNT;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;

    private final ClientDataStorageService clientDataStorageService;
    private final AccountInfoLoaderService accountInfoLoaderService;

    private final Logger logger = Logger.getLogger(PersonalityController.class);

    @Autowired
    public PersonalityController(ClientDataStorageService clientDataStorageService, AccountInfoLoaderService accountInfoLoaderService) {
        this.clientDataStorageService = clientDataStorageService;
        this.accountInfoLoaderService = accountInfoLoaderService;
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

//        TODO FILTER! Сделать аннотацию, аспект мб. Который проверяет данные на совпадение.
        if (request.getSession().getAttribute(ID_ACCOUNT) == null ||
                (int) request.getSession().getAttribute(ID_ACCOUNT) != idAccount) {
            throw new IllegalAccessException("User with id " + request.getSession().getAttribute(ID_ACCOUNT) + " trying " +
                    "to open page of user with id " + idAccount + "!");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PERSONALITY_PAGE);
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);

        Account account = accountInfoLoaderService.executeAndGetOne();
        ObjectMapper mapper = new ObjectMapper();
        String accountJSON = null;
        try {
            accountJSON = mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            //            TODO EXCEPTION
            e.printStackTrace();
        }

        modelAndView.addObject(ACCOUNT, accountJSON);

        return modelAndView;
    }
}
