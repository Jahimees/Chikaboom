package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Service;
import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.model.database.ServiceType;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.ServiceRepository;
import net.chikaboom.repository.ServiceSubtypeRepository;
import net.chikaboom.repository.ServiceTypeRepository;
import org.apache.log4j.Logger;
import org.springframework.security.acls.model.AlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис предоставляет возможность обработки данных пользовательских услуг
 */
@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceDataService implements DataService<Service> {

    private final ServiceRepository serviceRepository;
    private final AccountRepository accountRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceSubtypeRepository serviceSubtypeRepository;

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Производит поиск услуги по идентификатору.
     *
     * @param idService идентификатор услуги
     * @return найденную услугу
     */
    @Override
    public Optional<Service> findById(int idService) {
        logger.info("Loading service info with id " + idService);

        return serviceRepository.findById(idService);
    }

    /**
     * Производит поиск всех услуг.
     *
     * @return список всех услуг
     */
    @Override
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    /**
     * Удаляет пользовательскую услугу по её идентификатору.
     *
     * @param idService идентификатор услуги
     */
    @Override
    public void deleteById(int idService) {
        serviceRepository.deleteById(idService);
    }

    /**
     * Обновляет (полностью заменяет) услугу
     *
     * @param service новый объект услуги
     * @return измененный объект
     */
    @Override
    public Service update(Service service) {
        return serviceRepository.save(service);
    }

    /**
     * Создаёт новую пользовательскую услугу
     *
     * @param service создаваемый объект услуги
     * @return созданный объект
     */
    @Override
    public Service create(Service service)
    {
        if (isServiceExists(service)) {
            throw new AlreadyExistsException("This service already exists");
        }
        service.setIdService(0);

        return serviceRepository.save(service);
    }

    /**
     * Выполняет поиск всех услуг определенного мастера
     *
     * @return коллекцию пользовательских услуг (услуг мастера)
     */
    public List<Service> findAllServicesByIdAccount(int idAccount) {
        logger.info("Searching all services of user with id " + idAccount);
        Account account = accountRepository.findById(idAccount).
                orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));

        return serviceRepository.findAllByAccount(account);
    }

    /**
     * Производит поиск всех пользовательских услуг, которые соответствуют представленному перечню id подуслуг.
     * Т.е., если пользовательская услуга относится к подуслуге, которая включена в перечень поиска, то она будет найдена.
     *
     * @return коллекцию пользовательских услуг
     */
    public List<Service> findServicesByServiceSubtypeIds(int[] serviceSubtypeIdArray, int idServiceType) {
        logger.info("Searching all services which match their idServiceSubtype to serviceSubtypeIdArray");

        List<Service> serviceList = new ArrayList<>();

        if (serviceSubtypeIdArray != null && serviceSubtypeIdArray.length != 0) {
            logger.info("Found " + serviceSubtypeIdArray.length + " selected service subtypes. Searching data...");
            for (int idServiceSubtype : serviceSubtypeIdArray) {
                ServiceSubtype serviceSubtype = serviceSubtypeRepository.findById(idServiceSubtype).
                        orElseThrow(() -> new NoSuchDataException("Cannot find service subtype with"));
                if (serviceSubtype.getServiceType().getIdServiceType() == idServiceType) {
                    serviceList.addAll(serviceRepository.findAllByServiceSubtype(serviceSubtype));
                }
            }
        } else {
            logger.info("No one service subtype selected. Searching all services with idServiceType " + idServiceType);

            ServiceType serviceType = serviceTypeRepository.findById(idServiceType).
                    orElseThrow(() -> new NoSuchDataException("Cannot find service with id " + idServiceType));
            List<ServiceSubtype> serviceSubtypeList = serviceSubtypeRepository.findAllByServiceType(serviceType);

            for (ServiceSubtype serviceSubtype : serviceSubtypeList) {
                serviceList.addAll(serviceRepository.findAllByServiceSubtype(serviceSubtype));
            }
        }

        return serviceList;
    }

    /**
     * Производит проверку на существование сервиса в базе данных.
     *
     * @param service проверяемая услуга
     * @return true - если сервис уже существует, false - в противном случае
     */
    public boolean isServiceExists(Service service) {
        return serviceRepository.existsById(service.getIdService());
    }
}
