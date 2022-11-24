package net.chikaboom.service.action.tab;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AboutRepository;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.repository.RoleRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.HashPasswordService;
import net.chikaboom.service.action.DataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для изменения данных клиентом на странице "Настройки - основные"
 */
@Service
@PropertySource("/constants.properties")
public class EditSettingsTabService implements DataService {

    @Value("${attr.customAccount}")
    private String CUSTOM_ACCOUNT;

    private final ClientDataStorageService clientDataStorageService;
    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;
    private final HashPasswordService hashPasswordService;
    private final AboutRepository aboutRepository;
    private final RoleRepository roleRepository;

    private final Logger logger = Logger.getLogger(EditSettingsTabService.class);

    @Autowired
    public EditSettingsTabService(ClientDataStorageService clientDataStorageService, AccountRepository accountRepository,
                                  PhoneCodeRepository phoneCodeRepository, HashPasswordService hashPasswordService,
                                  AboutRepository aboutRepository, RoleRepository roleRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.accountRepository = accountRepository;
        this.phoneCodeRepository = phoneCodeRepository;
        this.hashPasswordService = hashPasswordService;
        this.aboutRepository = aboutRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Из полученных данных определяет, какое поле было изменено, а затем изменяет его в базе данных и возвращает
     * преобразованный объект
     *
     * @return новый сохраненный объект
     */
    @Override
    public Account executeAndGetOne() {
        logger.info("Starting to change account parameter...");
        Map<String, Object> accountParameters = (Map<String, Object>) clientDataStorageService.getData(CUSTOM_ACCOUNT);

        ObjectMapper mapper = new ObjectMapper();
        Account accountFromParameters = mapper.convertValue(accountParameters, Account.class);

        About about = aboutRepository.saveAndFlush(accountFromParameters.getAbout());
        accountFromParameters.setAbout(about);
        accountRepository.save(accountFromParameters);
        roleRepository.save(accountFromParameters.getRole());
        phoneCodeRepository.save(accountFromParameters.getPhoneCode());

        logger.info("Account data saved");
        return accountFromParameters;
    }
}