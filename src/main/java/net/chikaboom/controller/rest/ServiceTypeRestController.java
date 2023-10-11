package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.ServiceTypeFacade;
import net.chikaboom.model.database.Service;
import net.chikaboom.service.data.ServiceTypeDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<ServiceTypeFacade> findServiceType(@PathVariable int idServiceType) {
        return ResponseEntity.ok(serviceTypeDataService.findById(idServiceType));
    }

    /**
     * Производит поиск всех типов сервисов.
     *
     * @return список всех типов сервисов в формате json
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<ServiceTypeFacade>> findAllServiceTypes() {
        return ResponseEntity.ok(serviceTypeDataService.findAll());
    }
}
