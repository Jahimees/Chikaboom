package net.chikaboom.service.action.tab;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.HashPasswordService;
import net.chikaboom.service.action.DataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Map;

import static net.chikaboom.util.constant.EOFieldsCostant.CONVERTED_PASSWORD;
import static net.chikaboom.util.constant.EOFieldsCostant.SALT;

/**
 * Сервис для изменения данных клиентом на странице "Настройки - основные"
 */
@Service
@PropertySource("/constants.properties")
public class EditGeneralSettingsService implements DataService {

    @Value("${attr.customAccount}")
    private String CUSTOM_ACCOUNT;
    @Value("${attr.newPassword}")
    private String NEW_PASSWORD;
    @Value("${attr.oldPassword}")
    private String OLD_PASSWORD;
    @Value("${attr.confirmNewPassword}")
    private String CONFIRM_NEW_PASSWORD;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;
    @Value("${attr.phone}")
    private String PHONE;

    private final ClientDataStorageService clientDataStorageService;
    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;
    private final HashPasswordService hashPasswordService;

    private final Logger logger = Logger.getLogger(EditGeneralSettingsService.class);

    @Autowired
    public EditGeneralSettingsService(ClientDataStorageService clientDataStorageService, AccountRepository accountRepository,
                                      PhoneCodeRepository phoneCodeRepository, HashPasswordService hashPasswordService) {
        this.clientDataStorageService = clientDataStorageService;
        this.accountRepository = accountRepository;
        this.phoneCodeRepository = phoneCodeRepository;
        this.hashPasswordService = hashPasswordService;
    }

    /**
     * Из полученных данных определяет, какое поле было изменено, а затем изменяет его в базе данных и возвращает
     * преобразованный объект
     *
     * @return новый сохраненный объект
     */
    @Override
    public Account execute() {
        logger.info("Starting to change account parameter...");
        Map<String, Object> accountParameters = (Map<String, Object>) clientDataStorageService.getData(CUSTOM_ACCOUNT);

        Account resultAccount;
        ObjectMapper mapper = new ObjectMapper();
        resultAccount = mapper.convertValue(accountParameters, Account.class);

        if (accountParameters.get(PHONE_CODE) != null) {
            String phoneCode = accountParameters.get(PHONE_CODE).toString();
            String phone = accountParameters.get(PHONE).toString();
            int idPhoneCode = phoneCodeRepository.findOneByPhoneCode(Integer.parseInt(phoneCode)).getIdPhoneCode();

            resultAccount.setPhone(phone);
            resultAccount.setIdPhoneCode(idPhoneCode);

        } else if (accountParameters.get(NEW_PASSWORD) != null &&
                accountParameters.get(CONFIRM_NEW_PASSWORD).equals(accountParameters.get(NEW_PASSWORD))) {

            String actualPassword = hashPasswordService.convertPasswordForComparing(
                    accountParameters.get(OLD_PASSWORD).toString(), resultAccount.getSalt());

            if (resultAccount.getPassword().equals(actualPassword)) {
                Map<String, Object> complexPassword = hashPasswordService.convertPasswordForStorage
                        (accountParameters.get(NEW_PASSWORD).toString());

                resultAccount.setPassword(complexPassword.get(CONVERTED_PASSWORD).toString());
                resultAccount.setSalt(complexPassword.get(SALT).toString());
            } else {
                logger.info("Password or phone is incorrect.");

                throw new IncorrectInputDataException("Phone and/or password are/is incorrect");
            }
        }

        logger.info("Sending changes to database...");
        return accountRepository.save(resultAccount);
    }
}
