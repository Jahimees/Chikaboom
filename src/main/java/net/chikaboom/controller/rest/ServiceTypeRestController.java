package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.ServiceTypeFacadeConverter;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.facade.dto.ServiceTypeFacade;
import net.chikaboom.model.database.Service;
import net.chikaboom.model.database.ServiceType;
import net.chikaboom.model.response.CustomResponseObject;
import net.chikaboom.service.data.ServiceTypeDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link Service}
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/service-types")
public class ServiceTypeRestController {

    private final ServiceTypeDataService serviceTypeDataService;

    /**
     * Производит поиск типа услуги по её идентификатору.
     *
     * @param idServiceType идентификатор типа услуги
     * @return тип услаги в json-формате
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/{idServiceType}")
    public ResponseEntity<Facade> findServiceType(@PathVariable int idServiceType) {

        Optional<ServiceType> serviceTypeOptional = serviceTypeDataService.findById(idServiceType);

        if (!serviceTypeOptional.isPresent()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "There not found serviceType with id " + idServiceType,
                    "GET:/service-types/" + idServiceType
            ), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ServiceTypeFacadeConverter.convertToDto(serviceTypeOptional.get()));
    }

    /**
     * Производит поиск всех типов сервисов.
     *
     * @return список всех типов сервисов в формате json
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<ServiceTypeFacade>> findAllServiceTypes() {
        return ResponseEntity.ok(serviceTypeDataService.findAll()
                .stream().map(ServiceTypeFacadeConverter::convertToDto).collect(Collectors.toList()));
    }
}
