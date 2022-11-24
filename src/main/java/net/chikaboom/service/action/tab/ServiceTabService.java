package net.chikaboom.service.action.tab;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Subservice;
import net.chikaboom.model.database.UserService;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.SubserviceRepository;
import net.chikaboom.repository.UserServiceRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с данными услуг
 */
@Service
public class ServiceTabService implements DataService {

    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.userService}")
    private String USER_SERVICE;
    @Value("${attr.idUserService}")
    private String ID_USER_SERVICE;

    private final UserServiceRepository userServiceRepository;
    private final ClientDataStorageService clientDataStorageService;
    private final AccountRepository accountRepository;
    private final SubserviceRepository subserviceRepository;

    @Autowired
    public ServiceTabService(UserServiceRepository userServiceRepository, ClientDataStorageService clientDataStorageService,
                             AccountRepository accountRepository,
                             SubserviceRepository subserviceRepository) {
        this.userServiceRepository = userServiceRepository;
        this.clientDataStorageService = clientDataStorageService;
        this.accountRepository = accountRepository;
        this.subserviceRepository = subserviceRepository;
    }

    /**
     * Выполняет поиск всех услуг пользователя
     *
     * @return коллекцию пользовательских услуг
     */
    @Override
    public List<UserService> executeAndGetList() {
        int idAccount = (int) clientDataStorageService.getData(ID_ACCOUNT);
        Account account = accountRepository.findById(idAccount).
                orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));;

        return userServiceRepository.findAllByAccount(account);
    }

    /**
     * Выполняет поиск всех возможных подуслуг
     *
     * @return коллекцию подуслуг
     */
    public List<Subservice> findAllSubservices() {
        return subserviceRepository.findAll();
    }

    /**
     * Сохраняет/создает пользовательскую услугу
     *
     * @return созданную услугу
     */
    public UserService saveUserService() {
        UserService userService = (UserService) clientDataStorageService.getData(USER_SERVICE);

        return userServiceRepository.save(userService);
    }

    /**
     * Удаляет пользовательскую услугу
     */
    public void deleteUserService() {
        int idUserService = (int) clientDataStorageService.getData(ID_USER_SERVICE);

        userServiceRepository.deleteById(idUserService);
    }
}
