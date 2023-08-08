package net.chikaboom.service.action.tab;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AboutRepository;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для изменения данных клиентом на странице "Настройки - основные"
 */
@Service
public class EditSettingsTabService {

    @Value("${attr.oldPassword}")
    private String OLD_PASSWORD;
    @Value("${attr.newPassword}")
    private String NEW_PASSWORD;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;

    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AboutRepository aboutRepository;

    private final Logger logger = Logger.getLogger(EditSettingsTabService.class);

    @Autowired
    public EditSettingsTabService(AccountRepository accountRepository, PhoneCodeRepository phoneCodeRepository,
                                  BCryptPasswordEncoder passwordEncoder,
                                  AboutRepository aboutRepository) {
        this.accountRepository = accountRepository;
        this.phoneCodeRepository = phoneCodeRepository;
        this.aboutRepository = aboutRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Из полученных данных определяет, какое поле было изменено, а затем изменяет его в базе данных и возвращает
     * преобразованный объект
     *
     * @return новый сохраненный объект
     */
    public Account updateAccountSettings(Map<String, Object> accountParameters) {
        logger.info("Starting to change account parameter.");

//        TODO REFACTORING
        ObjectMapper mapper = new ObjectMapper();
        if (accountParameters.get(PHONE_CODE).getClass() == String.class) {
            accountParameters.put(PHONE_CODE,
                    phoneCodeRepository.findFirstByPhoneCode(
                            Integer.parseInt(accountParameters.get(PHONE_CODE).toString())
                    )
            );
        }

        Account accountFromParameters = mapper.convertValue(accountParameters, Account.class);

        if (accountParameters.get(NEW_PASSWORD) != null) {
            savePasswordData(accountParameters, accountFromParameters);
        }

        About about = aboutRepository.saveAndFlush(accountFromParameters.getAbout() != null
                ? accountFromParameters.getAbout() : new About());
        accountFromParameters.setAbout(about);
        accountRepository.save(accountFromParameters);
        phoneCodeRepository.save(accountFromParameters.getPhoneCode());

        logger.info("Account data saved");
        return accountFromParameters;
    }

    /**
     * Производит операции сравнения паролей и преобразование нового пароля для хранения в базе данных
     *
     * @param accountParameters измененные параметры аккаунта
     * @param resultAccount     конечный объект для сохранения
     */
    private void savePasswordData(Map<String, Object> accountParameters, Account resultAccount) {
        String actualPassword = accountParameters.get(OLD_PASSWORD).toString();

        if (passwordEncoder.matches(actualPassword, resultAccount.getPassword())) {
            String newPassword = passwordEncoder.encode(accountParameters.get(NEW_PASSWORD).toString());
            resultAccount.setPassword(newPassword);
        } else {
            logger.info("Password is incorrect.");

            throw new IncorrectInputDataException("password is incorrect");
        }
    }
}