package net.chikaboom.service.action.tab;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.PhoneCode;
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
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для изменения данных клиентом на странице "Настройки - основные"
 */
@Service
public class EditSettingsTabService implements DataService {

    @Value("${attr.customAccount}")
    private String CUSTOM_ACCOUNT;
    @Value("${attr.oldPassword}")
    private String OLD_PASSWORD;
    @Value("${attr.newPassword}")
    private String NEW_PASSWORD;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;
    @Value("${attr.phone}")
    private String PHONE;

    @Value("${converted_password}")
    private String CONVERTED_PASSWORD;
    @Value("${salt}")
    private String SALT;

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

        Account accountFromParameters = new Account();

        ObjectMapper mapper = new ObjectMapper();
        if (accountParameters.get(PHONE_CODE).getClass() == String.class) {
            savePhoneData(accountParameters, accountFromParameters);
        }

        accountFromParameters = mapper.convertValue(accountParameters, Account.class);

        if (accountParameters.get(NEW_PASSWORD) != null) {
            savePasswordData(accountParameters, accountFromParameters);
        }

        About about = aboutRepository.saveAndFlush(accountFromParameters.getAbout() != null ? accountFromParameters.getAbout() : new About());
        accountFromParameters.setAbout(about);
        accountRepository.save(accountFromParameters);
        roleRepository.save(accountFromParameters.getRole());
        phoneCodeRepository.save(accountFromParameters.getPhoneCode());

        logger.info("Account data saved");
        return accountFromParameters;
    }

    private void savePhoneData(Map<String, Object> accountParameters, Account resultAccount) {
        String phoneCode = accountParameters.get(PHONE_CODE).toString();
        String phone = accountParameters.get(PHONE).toString();
        PhoneCode foundPhoneCode = phoneCodeRepository.findOneByPhoneCode(Integer.parseInt(phoneCode));

        accountParameters.put(PHONE_CODE, foundPhoneCode);
        resultAccount.setPhone(phone);
        resultAccount.setPhoneCode(foundPhoneCode);
    }

    private void savePasswordData(Map<String, Object> accountParameters, Account resultAccount) {
        String actualPassword = hashPasswordService.convertPasswordForComparing(
                accountParameters.get(OLD_PASSWORD).toString(), resultAccount.getSalt());

        if (resultAccount.getPassword().equals(actualPassword)) {
            Map<String, Object> complexPassword = hashPasswordService.convertPasswordForStorage
                    (accountParameters.get(NEW_PASSWORD).toString());

            resultAccount.setPassword(complexPassword.get(CONVERTED_PASSWORD).toString());
            resultAccount.setSalt(complexPassword.get(SALT).toString());
        } else {
            logger.info("Password is incorrect.");

            throw new IncorrectInputDataException("password is incorrect");
        }
    }
}