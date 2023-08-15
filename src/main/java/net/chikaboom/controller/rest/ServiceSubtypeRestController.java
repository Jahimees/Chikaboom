package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.service.data.ServiceSubtypeDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<ServiceSubtype> findServiceSubtype(int idServiceSubtype) {
        Optional<ServiceSubtype> serviceSubtype = serviceSubtypeDataService.findById(idServiceSubtype);

        return serviceSubtype.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Возвращает все возможные подтипы услуг.
     *
     * @return список всех возможных подтипов услуг
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/service-subtypes")
    public ResponseEntity<List<ServiceSubtype>> findAllServices() {
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
    public ResponseEntity<List<ServiceSubtype>> findAllServiceSubtypesByIdServiceType(@PathVariable int idServiceType) {
        List<ServiceSubtype> serviceSubtypeList;
        try {
            serviceSubtypeList = serviceSubtypeDataService.findAllServiceSubtypesByIdServiceType(idServiceType);
        } catch (NoSuchDataException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(serviceSubtypeList);
    }
}
