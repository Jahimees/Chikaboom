package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.model.database.Service;
import net.chikaboom.service.data.ServiceDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link Service}
 */
@RequiredArgsConstructor
@RestController
public class ServiceRestController {

    private final ServiceDataService serviceDataService;

    /**
     * Производит поиск услуге по его id.
     *
     * @param idService идентификатор услуги
     * @return услуга в json-формате
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/services/{idService}")
    public ResponseEntity<Service> findService(@PathVariable int idService) {
        Optional<Service> serviceOptional = serviceDataService.findById(idService);

        return serviceOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Производит поиск всех возможных услуг. Необходимо быть авторизованным.
     *
     * @return все пользовательские услуги.
     * @deprecated нет необходимости в поиске сразу всех сервисов
     */
    @Deprecated
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/services")
    public ResponseEntity<List<Service>> findAllServices() {
        return ResponseEntity.ok(serviceDataService.findAll());
    }

    /**
     * Создает данные об услуге мастера. Необходимо иметь роль мастера. Невозможно создать услугу другому пользователю
     *
     * @param service услуга, которую предоставляет мастер
     * @return созданная услуга
     */
    @PreAuthorize("hasRole('MASTER')")
    @PostMapping("/services")
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        Account masterAccount = service.getAccount();
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getIdAccount() != masterAccount.getIdAccount()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (service.getAccount().getIdAccount() != principal.getIdAccount()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(serviceDataService.create(service));
    }

    /**
     * Обновляет данные об услуге мастера (полная замена). Необходимо иметь роль мастера. Невозможно изменить услугу
     * другому пользователю.
     *
     * @param service услуга, которую предоставляет мастер
     * @return обновленная услуга
     */
    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/services/{idService}")
    public ResponseEntity<Service> replaceService(@PathVariable int idService, @RequestBody Service service) {
        Optional<Service> serviceOptional = serviceDataService.findById(idService);

        if (!serviceOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (service.getAccount().getIdAccount() != principal.getIdAccount()
                || serviceOptional.get().getAccount().getIdAccount() != principal.getIdAccount()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        service.setIdService(idService);

        return ResponseEntity.ok(serviceDataService.update(service));
    }

    /**
     * Удаляет из базы данных указанную услугу. Необходимо иметь роль мастера. Невозможно удалить чужую услугу.
     *
     * @param idService идентификатор услуги, которую необходимо удалить
     * @return json-ответ с кодом
     */
    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/services/{idService}")
    public ResponseEntity<Object> deleteService(@PathVariable int idService) {
        Optional<Service> serviceOptional = serviceDataService.findById(idService);

        if (!serviceOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        CustomPrincipal customPrincipal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (serviceOptional.get().getAccount().getIdAccount() != customPrincipal.getIdAccount()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        serviceDataService.deleteById(idService);

        return ResponseEntity.ok().build();
    }

    /**
     * Загружает информацию обо всех услугах, которыесоздал пользователь.
     *
     * @param idAccount идентификатор аккаунта
     * @return полную информацию в формате JSON обо всех созданных пользователем услугах
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/accounts/{idAccount}/services")
    public ResponseEntity<List<Service>> findAllServicesByIdAccount(@PathVariable int idAccount) {
        return ResponseEntity.ok(serviceDataService.findAllServicesByIdAccount(idAccount));
    }

    /**
     * Поиск всех услуг по перечню подтипов услуг конкретного типа услуги.
     * Выбирается тип услуги (напр. Барбершоп), выбирается несколько подтипом (Стрижка бороды, усов) и по этим
     * параметрам производится поиск созданных пользовательских услуг.
     * В случае, если передается пустой массив идентификаторов подтипов услуг, производится поиск всех услуг
     * только по типу услуг.
     * Если в массиве присутствуют идентификаторы подтипов услуг, которые не относятся к выбранной услуге,
     * то они игнорируются.
     *
     * @param idServiceType     идентификатор типа услуги
     * @param serviceSubtypeIds массив идентификаторов подтипов услуг
     * @return список найденных услуг в формате json
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/service-types/{idServiceType}/service-subtypes/services")
    public ResponseEntity<List<Service>> findAllServicesByServiceSubtypeIds(
            @PathVariable int idServiceType, @RequestParam int[] serviceSubtypeIds) {
        List<Service> serviceList;
        try {
            serviceList = serviceDataService.findServicesByServiceSubtypeIds(serviceSubtypeIds, idServiceType);
        } catch (NoSuchDataException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(serviceList);
    }
}
