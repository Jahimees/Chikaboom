package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Service;
import net.chikaboom.model.database.ServiceType;
import net.chikaboom.service.data.ServiceTypeDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<ServiceType> findServiceType(@PathVariable int idServiceType) {
        Optional<ServiceType> serviceTypeOptional = serviceTypeDataService.findById(idServiceType);

        return serviceTypeOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Производит поиск всех типов сервисов.
     *
     * @return список всех типов сервисов в формате json
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<ServiceType>> findAllServiceTypes() {
        return ResponseEntity.ok(serviceTypeDataService.findAll());
    }
}
