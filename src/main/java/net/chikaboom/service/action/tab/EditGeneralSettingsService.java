package net.chikaboom.service.action.tab;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Address;
import net.chikaboom.repository.AboutRepository;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.AddressRepository;
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
import java.util.Optional;

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
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
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
    @Value("${attr.aboutText}")
    private String ABOUT_TEXT;
    @Value("${attr.aboutTags}")
    private String ABOUT_TAGS;
    @Value("${attr.address}")
    private String ADDRESS;
    @Value("${attr.profession}")
    private String PROFESSION;

    private final ClientDataStorageService clientDataStorageService;
    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;
    private final HashPasswordService hashPasswordService;
    private final AboutRepository aboutRepository;
    private final AddressRepository addressRepository;

    private final Logger logger = Logger.getLogger(EditGeneralSettingsService.class);

    @Autowired
    public EditGeneralSettingsService(ClientDataStorageService clientDataStorageService, AccountRepository accountRepository,
                                      PhoneCodeRepository phoneCodeRepository, HashPasswordService hashPasswordService,
                                      AboutRepository aboutRepository, AddressRepository addressRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.accountRepository = accountRepository;
        this.phoneCodeRepository = phoneCodeRepository;
        this.hashPasswordService = hashPasswordService;
        this.aboutRepository = aboutRepository;
        this.addressRepository = addressRepository;
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
            savePhoneData(accountParameters, resultAccount);

        } else if (accountParameters.get(NEW_PASSWORD) != null &&
                accountParameters.get(CONFIRM_NEW_PASSWORD).equals(accountParameters.get(NEW_PASSWORD))) {
            savePasswordData(accountParameters, resultAccount);

        } else if (accountParameters.get(ABOUT_TEXT) != null || accountParameters.get(PROFESSION) != null) {
            saveAboutData(accountParameters);

        } else if (accountParameters.get(ADDRESS) != null) {
            saveAddressData(accountParameters);

        }

        logger.info("Sending changes to database...");
        return accountRepository.save(resultAccount);
    }

    private void savePhoneData(Map<String, Object> accountParameters, Account resultAccount) {
        String phoneCode = accountParameters.get(PHONE_CODE).toString();
        String phone = accountParameters.get(PHONE).toString();
        int idPhoneCode = phoneCodeRepository.findOneByPhoneCode(Integer.parseInt(phoneCode)).getIdPhoneCode();

        resultAccount.setPhone(phone);
        resultAccount.setIdPhoneCode(idPhoneCode);
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
            logger.info("Password or phone is incorrect.");

            throw new IncorrectInputDataException("Phone and/or password are/is incorrect");
        }
    }

    private void saveAboutData(Map<String, Object> accountParameters) {
        String aboutText = accountParameters.get(ABOUT_TEXT) == null ? "" : accountParameters.get(ABOUT_TEXT).toString();
        String aboutTags = accountParameters.get(ABOUT_TAGS) == null ? "" : accountParameters.get(ABOUT_TAGS).toString();
        String profession = accountParameters.get(PROFESSION) == null ? "" : accountParameters.get(PROFESSION).toString();

        About about = new About();
        Optional<About> aboutOptional = aboutRepository.findByIdAccount((Integer) accountParameters.get(ID_ACCOUNT));

        if (aboutOptional.isPresent()) {
            about = aboutOptional.get();
        } else {
            about.setIdAccount((Integer) accountParameters.get(ID_ACCOUNT));
        }

        about.setText(aboutText);
        about.setTags(aboutTags);
        about.setProfession(profession);

        aboutRepository.save(about);
    }

    private void saveAddressData(Map<String, Object> accountParameters) {
        String addressText = accountParameters.get(ADDRESS).toString();

        Address address = new Address();
        Optional<Address> addressOptional = addressRepository.findByIdAccount((Integer) accountParameters.get(ID_ACCOUNT));
        if (addressOptional.isPresent()) {
            address = addressOptional.get();

        } else {
            address.setIdAccount((Integer) accountParameters.get(ID_ACCOUNT));

        }

        address.setAddress(addressText);

        addressRepository.save(address);
    }
}
