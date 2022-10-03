package net.chikaboom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@PropertySource("/constants.properties")
@RequestMapping("/chikaboom/personality/{idAccount}")
public class PersonalityController {

    @Value("${attr.account}")
    private String ACCOUNT;

    private final AccountRepository accountRepository;

    private final Logger logger = Logger.getLogger(PersonalityController.class);

    @Autowired
    public PersonalityController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Value("${page.personality}")
    private String PERSONALITY_PAGE;

    @GetMapping
    public ModelAndView openPersonalityPage(@PathVariable int idAccount) {
        logger.info("Opening personality page");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PERSONALITY_PAGE);
        ObjectMapper objectMapper = new ObjectMapper();

        String accountJson = "";

        try {
            accountJson = objectMapper.writeValueAsString(accountRepository.findById(idAccount).get());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
//            TODO Exception
        }

        modelAndView.addObject(ACCOUNT, accountJson);
// TODO NoSuchElementException
        return modelAndView;
    }
}
