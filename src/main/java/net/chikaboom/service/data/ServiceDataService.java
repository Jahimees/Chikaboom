package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.facade.converter.AccountFacadeConverter;
import net.chikaboom.facade.converter.ServiceFacadeConverter;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.ServiceFacade;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Service;
import net.chikaboom.repository.ServiceRepository;
import net.chikaboom.repository.specification.ServiceSpecifications;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис предоставляет возможность обработки данных пользовательских услуг
 */
@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceDataService implements DataService<ServiceFacade> {

    private final ServiceRepository serviceRepository;
    private final AccountDataService accountDataService;
    private final ServiceFacadeConverter serviceFacadeConverter;
    private final AccountFacadeConverter accountFacadeConverter;

    /**
     * Производит поиск услуги по идентификатору.
     *
     * @param idService идентификатор услуги
     * @return найденную услугу
     */
    @Override
    public ServiceFacade findById(int idService) {
        Optional<Service> serviceOptional = serviceRepository.findById(idService);

        if (!serviceOptional.isPresent()) {
            throw new NotFoundException("There not found service with id " + idService);
        }

        return serviceFacadeConverter.convertToDto(serviceOptional.get());
    }

    /**
     * Производит поиск всех услуг.
     *
     * @return список всех услуг
     */
    @Override
    public List<ServiceFacade> findAll() {
        return serviceRepository.findAll().stream().map(
                serviceFacadeConverter::convertToDto).collect(Collectors.toList());
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
     * @param serviceFacade новый объект услуги
     * @return измененный объект
     */
    @Override
    public ServiceFacade update(ServiceFacade serviceFacade) {
        Optional<Service> serviceOptional = serviceRepository.findById(serviceFacade.getIdService());

        if (!serviceOptional.isPresent()) {
            throw new NotFoundException("There not found service with id " + serviceFacade.getIdService());
        }

        return serviceFacadeConverter.convertToDto(
                serviceRepository.save(
                        serviceFacadeConverter.convertToModel(serviceFacade)));
    }

    /**
     * Создаёт новую пользовательскую услугу
     *
     * @param serviceFacade создаваемый объект услуги
     * @return созданный объект
     */
    @Override
    public ServiceFacade create(ServiceFacade serviceFacade) {
        if (isServiceExists(serviceFacade)) {
            throw new AlreadyExistsException("This service already exists");
        }
        serviceFacade.setIdService(0);

        return serviceFacadeConverter.convertToDto(
                serviceRepository.save(
                        serviceFacadeConverter.convertToModel(serviceFacade)));
    }

    /**
     * Выполняет поиск всех услуг определенного мастера
     *
     * @return коллекцию пользовательских услуг (услуг мастера)
     */
    public List<ServiceFacade> findAllServicesByIdAccount(int idAccount) {
        AccountFacade accountFacade = accountDataService.findById(idAccount);

        return serviceRepository
                .findAllByAccount(accountFacadeConverter.convertToModel(accountFacade))
                .stream().map(serviceFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Производит поиск всех пользовательских услуг, которые соответствуют представленному перечню id подуслуг.
     * Т.е., если пользовательская услуга относится к подуслуге, которая включена в перечень поиска, то она будет найдена.
     *
     * @return коллекцию пользовательских услуг
     */
    public List<ServiceFacade> findServicesByServiceSubtypeIds(int[] serviceSubtypeIdArray, int idServiceType) {
        List<Service> serviceList;
        serviceList = serviceRepository.findAll(
                ServiceSpecifications.servicesByServiceSubtypeIdArray(serviceSubtypeIdArray, idServiceType)
        );

        return serviceList.stream().map(serviceFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Производит проверку на существование сервиса в базе данных.
     *
     * @param serviceFacade проверяемая услуга
     * @return true - если сервис уже существует, false - в противном случае
     */
    public boolean isServiceExists(ServiceFacade serviceFacade) {
        return serviceRepository.existsById(serviceFacade.getIdService());
    }
}
