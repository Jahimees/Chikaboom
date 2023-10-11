package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.facade.dto.ServiceSubtypeFacade;
import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.model.response.CustomResponseObject;
import net.chikaboom.service.data.ServiceSubtypeDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link ServiceSubtype}
 */
@RequiredArgsConstructor
@RestController
public class ServiceSubtypeRestController {

    private final ServiceSubtypeDataService serviceSubtypeDataService;

    /**
     * Производит поиск подтипа услуги по её идентификатору.
     *
     * @param idServiceSubtype идентификатор подтипа услуги
     * @return найденный подтип услуги в формате json
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/service-subtypes/{idServiceSubtype}")
    public ResponseEntity<ServiceSubtypeFacade> findServiceSubtype(int idServiceSubtype) {
        return ResponseEntity.ok(serviceSubtypeDataService.findById(idServiceSubtype));
    }

    /**
     * Возвращает все возможные подтипы услуг.
     *
     * @return список всех возможных подтипов услуг
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/service-subtypes")
    public ResponseEntity<List<ServiceSubtypeFacade>> findAllServiceSubtypes() {
        return ResponseEntity.ok(serviceSubtypeDataService.findAll());
    }

    /**
     * Производит поиск всех подтипов услуг по идентификатору типа услуги.
     *
     * @param idServiceType идентификатор типа услуги
     * @return список подтипов услуг, соответствующих выбранному типау услуг в формате json
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/service-types/{idServiceType}/service-subtypes")
    public ResponseEntity<List<? extends Facade>> findAllServiceSubtypesByIdServiceType(@PathVariable int idServiceType) {
        List<ServiceSubtypeFacade> serviceSubtypeList;
        try {
            serviceSubtypeList = serviceSubtypeDataService.findAllServiceSubtypesByIdServiceType(idServiceType);
        } catch (NoSuchDataException e) {

            return new ResponseEntity<>(List.of(new CustomResponseObject(
                    HttpStatus.NOT_FOUND.value(),
                    "No one serviceSubtype found. Server may have problems. Please report it",
                    "GET:/service-types/" + idServiceType + "/service-subtypes"
            )), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(serviceSubtypeList);
    }
}
