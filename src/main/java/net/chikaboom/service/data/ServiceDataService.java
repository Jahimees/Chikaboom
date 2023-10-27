package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Service;
import net.chikaboom.repository.ServiceRepository;
import net.chikaboom.repository.specification.ServiceSpecifications;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис предоставляет возможность обработки данных пользовательских услуг
 */
@RequiredArgsConstructor
@org.springframework.stereotype.Service
@Transactional
public class ServiceDataService implements DataService<Service> {

    private final ServiceRepository serviceRepository;
    private final AccountDataService accountDataService;

    /**
     * Производит поиск услуги по идентификатору.
     *
     * @param idService идентификатор услуги
     * @return найденную услугу
     */
    @Override
    public Optional<Service> findById(int idService) {
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
        Optional<Service> serviceOptional = serviceRepository.findById(service.getIdService());

        if (!serviceOptional.isPresent()) {
            throw new NotFoundException("There not found service with id " + service.getIdService());
        }

        return serviceRepository.save(service);
    }

    /**
     * Создаёт новую пользовательскую услугу
     *
     * @param service создаваемый объект услуги
     * @return созданный объект
     */
    @Override
    public Service create(Service service) {
        if (isServiceExists(service)) {
            throw new AlreadyExistsException("This service already exists");
        }

        service.setIdService(0);

        return serviceRepository.saveAndFlush(service);
    }

    /**
     * Выполняет поиск всех услуг определенного мастера
     *
     * @return коллекцию пользовательских услуг (услуг мастера)
     */
    public List<Service> findAllServicesByIdAccount(int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("There not found account with id " + idAccount);
        }

        Account accountFromDb = accountOptional.get();

        return serviceRepository.findAllByAccount(accountFromDb);
    }

    /**
     * Производит поиск всех пользовательских услуг, которые соответствуют представленному перечню id подуслуг.
     * Т.е., если пользовательская услуга относится к подуслуге, которая включена в перечень поиска, то она будет найдена.
     *
     * @return коллекцию пользовательских услуг
     */
    public List<Service> findServicesByServiceSubtypeIds(int[] serviceSubtypeIdArray, int idServiceType) {
        return serviceRepository.findAll(
                ServiceSpecifications.servicesByServiceSubtypeIdArray(serviceSubtypeIdArray, idServiceType));
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
