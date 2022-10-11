package net.chikaboom.controller;

import net.chikaboom.exception.IllegalAccessException;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.LoadPersonalityService;
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
import java.util.Map;


@RestController
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{idAccount}")
public class PersonalityController {

    @Value("${attr.account}")
    private String ACCOUNT;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;

    private final ClientDataStorageService clientDataStorageService;
    private final LoadPersonalityService loadPersonalityService;

    private final Logger logger = Logger.getLogger(PersonalityController.class);

    @Autowired
    public PersonalityController(ClientDataStorageService clientDataStorageService, LoadPersonalityService loadPersonalityService) {
        this.clientDataStorageService = clientDataStorageService;
        this.loadPersonalityService = loadPersonalityService;
    }

    @Value("${page.personality}")
    private String PERSONALITY_PAGE;

    @GetMapping
    public ModelAndView openPersonalityPage(@PathVariable int idAccount, HttpServletRequest request) throws IllegalAccessException {
        logger.info("Opening personality page...");

//        TODO FILTER! Сделать аннотацию, аспект мб я хз. Который проверяет данные на совпадение.
        if (request.getSession().getAttribute(ID_ACCOUNT) == null ||
                (int) request.getSession().getAttribute(ID_ACCOUNT) != idAccount) {
            throw new IllegalAccessException("User with id " + request.getSession().getAttribute(ID_ACCOUNT) + " trying " +
                    "to open page of user with id " + idAccount + "!");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PERSONALITY_PAGE);
        clientDataStorageService.setData(ID_ACCOUNT, idAccount);

        Map<String, Object> parameters = loadPersonalityService.execute();

        modelAndView.addObject(ACCOUNT, parameters.get(ACCOUNT));
        modelAndView.addObject(PHONE_CODE, parameters.get(PHONE_CODE));

        return modelAndView;
    }
}
