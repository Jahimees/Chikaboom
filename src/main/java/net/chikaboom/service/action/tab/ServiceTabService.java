package net.chikaboom.service.action.tab;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Service;
import net.chikaboom.model.database.Subservice;
import net.chikaboom.model.database.UserService;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.ServiceRepository;
import net.chikaboom.repository.SubserviceRepository;
import net.chikaboom.repository.UserServiceRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с данными услуг
 */
@org.springframework.stereotype.Service
public class ServiceTabService implements DataService {

    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.userService}")
    private String USER_SERVICE;
    @Value("${attr.idUserService}")
    private String ID_USER_SERVICE;
    @Value("${attr.idService}")
    private String ID_SERVICE;
    @Value("${attr.subserviceIdList}")
    private String SUBSERVICE_ID_LIST;

    private final UserServiceRepository userServiceRepository;
    private final ClientDataStorageService clientDataStorageService;
    private final AccountRepository accountRepository;
    private final SubserviceRepository subserviceRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceTabService(UserServiceRepository userServiceRepository, ClientDataStorageService clientDataStorageService,
                             AccountRepository accountRepository, ServiceRepository serviceRepository,
                             SubserviceRepository subserviceRepository) {
        this.userServiceRepository = userServiceRepository;
        this.clientDataStorageService = clientDataStorageService;
        this.accountRepository = accountRepository;
        this.subserviceRepository = subserviceRepository;
        this.serviceRepository = serviceRepository;
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
                orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));

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

    public List<Subservice> getSubservices() {
        int idService = (int) clientDataStorageService.getData(ID_SERVICE);
        Service service = serviceRepository.findById(idService).
                orElseThrow(() -> new NoSuchDataException("Cannot find service with id " + idService));

        return subserviceRepository.findAllByService(service);
    }

    public List<UserService> getUserServicesByServiceIds() {
        Integer[] subservices = (Integer[]) clientDataStorageService.getData(SUBSERVICE_ID_LIST);
        int idService = (int) clientDataStorageService.getData(ID_SERVICE);

        List<UserService> userServiceList = new ArrayList<>();

        if (subservices.length != 0) {
            for (int i = 0; i < subservices.length; i++) {
                Subservice subservice = subserviceRepository.findById(subservices[i]).
                        orElseThrow(() -> new NoSuchDataException("Cannot find subservice with"));
                userServiceList.addAll(userServiceRepository.findAllBySubservice(subservice));
            }
        } else {
            Service service = serviceRepository.findById(idService).
                    orElseThrow(() -> new NoSuchDataException("Cannot find service with id " + idService));
            List<Subservice> subserviceList = subserviceRepository.findAllByService(service);

            for (Subservice subservice : subserviceList) {
                userServiceList.addAll(userServiceRepository.findAllBySubservice(subservice));
            }
        }

        return userServiceList;
    }
}
