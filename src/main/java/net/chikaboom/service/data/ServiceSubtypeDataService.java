package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.ServiceSubtypeFacadeConverter;
import net.chikaboom.facade.converter.ServiceTypeFacadeConverter;
import net.chikaboom.facade.dto.ServiceSubtypeFacade;
import net.chikaboom.facade.dto.ServiceTypeFacade;
import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.repository.ServiceSubtypeRepository;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис предоставляет возможность обработки данных подтипа услугии.
 * Пользователям не дана возможность создавать свои подтипы услуг.
 */
@RequiredArgsConstructor
@Service
public class ServiceSubtypeDataService {

    private final ServiceSubtypeRepository serviceSubtypeRepository;
    private final ServiceTypeDataService serviceTypeDataService;
    private final ServiceSubtypeFacadeConverter serviceSubtypeFacadeConverter;
    private final ServiceTypeFacadeConverter serviceTypeFacadeConverter;

    /**
     * Производит поиск подтипа услуги по его идентификатору
     *
     * @param idServiceSubtype идентификатор подтипа услуги
     * @return найденный подтип услуги
     */
    public ServiceSubtypeFacade findById(int idServiceSubtype) {
        Optional<ServiceSubtype> serviceSubtypeOptional = serviceSubtypeRepository.findById(idServiceSubtype);

        if (!serviceSubtypeOptional.isPresent()) {
            throw new NotFoundException("There not found serviceSubtype with id " + idServiceSubtype);
        }

        return serviceSubtypeFacadeConverter.convertToDto(serviceSubtypeOptional.get());
    }

    /**
     * Производит поиск всех подтипов услуг.
     *
     * @return список всех подтипов услуг
     */
    public List<ServiceSubtypeFacade> findAll() {
        return serviceSubtypeRepository.findAll().stream().map(serviceSubtypeFacadeConverter::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Производит поиск всех подуслуг, которые относятся к данной услуге (idServiceType)
     *
     * @return коллекцию подуслуг
     */
    public List<ServiceSubtypeFacade> findAllServiceSubtypesByIdServiceType(int idServiceType) {
        ServiceTypeFacade serviceTypeFacade = serviceTypeDataService.findById(idServiceType);

        return serviceSubtypeRepository
                .findAllByServiceType(serviceTypeFacadeConverter.convertToModel(serviceTypeFacade))
                .stream().map(serviceSubtypeFacadeConverter::convertToDto).collect(Collectors.toList());
    }
}
