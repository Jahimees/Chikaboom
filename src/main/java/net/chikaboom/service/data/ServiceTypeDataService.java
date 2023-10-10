package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.ServiceTypeFacadeConverter;
import net.chikaboom.facade.dto.ServiceTypeFacade;
import net.chikaboom.model.database.ServiceType;
import net.chikaboom.repository.ServiceTypeRepository;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис предоставляет возможность обработки данных типа услуг.
 */
@RequiredArgsConstructor
@Service
public class ServiceTypeDataService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeFacadeConverter serviceTypeFacadeConverter;

    /**
     * Производит поиск типа услуги по его идентификатору.
     *
     * @param idServiceType идентификатор типа услуги
     * @return объект типа услуги
     */
    public ServiceTypeFacade findById(int idServiceType) {
        Optional<ServiceType> serviceTypeOptional = serviceTypeRepository.findById(idServiceType);

        if (!serviceTypeOptional.isPresent()) {
            throw new NotFoundException("There not found serviceType with id " + idServiceType);
        }

        return serviceTypeFacadeConverter.convertToDto(serviceTypeOptional.get());
    }

    /**
     * Производит поиск всех типов услуг.
     *
     * @return список всех типов услуг
     */
    public List<ServiceTypeFacade> findAll() {
        return serviceTypeRepository.findAll().stream().map(
                serviceTypeFacadeConverter::convertToDto).collect(Collectors.toList());
    }
}
