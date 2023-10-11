package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.facade.dto.ServiceFacade;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.model.database.Service;
import net.chikaboom.model.response.CustomResponseObject;
import net.chikaboom.service.data.ServiceDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
//    TODO выдает слишком много информации о мастере
    @PreAuthorize("permitAll()")
    @GetMapping("/services/{idService}")
    public ResponseEntity<ServiceFacade> findService(@PathVariable int idService) {
        ServiceFacade serviceFacade = serviceDataService.findById(idService);

        return ResponseEntity.ok(serviceFacade);
    }

    /**
     * Загружает информацию обо всех услугах, которыесоздал пользователь.
     *
     * @param idAccount идентификатор аккаунта
     * @return полную информацию в формате JSON обо всех созданных пользователем услугах
     */
//    TODO выдает слишком много информации (о мастере)
    @PreAuthorize("permitAll()")
    @GetMapping("/accounts/{idAccount}/services")
    public ResponseEntity<List<ServiceFacade>> findAllServicesByIdAccount(@PathVariable int idAccount) {
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
    public ResponseEntity<List<? extends Facade>> findAllServicesByServiceSubtypeIds(
            @PathVariable int idServiceType, @RequestParam int[] serviceSubtypeIds) {

        List<ServiceFacade> serviceList;
        try {
            serviceList = serviceDataService.findServicesByServiceSubtypeIds(serviceSubtypeIds, idServiceType);
        } catch (NoSuchDataException e) {

            return new ResponseEntity<>(List.of(
                    new CustomResponseObject(
                            HttpStatus.NOT_FOUND.value(),
                            "No one service found by serviceSubtype ids. Please check serviceSubtypeId array." +
                                    "May be ServiceType doesn't mathes with any one received serviceSubtypeId",
                            "GET:/service-types/" + idServiceType + "/service-subtypes/services"
                    )
            ), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(serviceList);
    }

    /**
     * Создает данные об услуге мастера. Необходимо иметь роль мастера. Невозможно создать услугу другому пользователю
     *
     * @param serviceFacade услуга, которую предоставляет мастер
     * @return созданная услуга
     */
    @PreAuthorize("hasRole('MASTER')")
    @PostMapping("/services")
    public ResponseEntity<Facade> createService(@RequestBody ServiceFacade serviceFacade) {
        AccountFacade masterAccountFacade = serviceFacade.getAccountFacade();
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getIdAccount() != masterAccountFacade.getIdAccount()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "Authenticated user is not matches with account in creation service",
                    "POST:/services"
            ), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(serviceDataService.create(serviceFacade));
    }

    /**
     * Обновляет данные об услуге мастера (полная замена). Необходимо иметь роль мастера. Невозможно изменить услугу
     * другому пользователю.
     *
     * @param serviceFacadeParam услуга, которую предоставляет мастер
     * @return обновленная услуга
     */
    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/services/{idService}")
    public ResponseEntity<Facade> replaceService(@PathVariable int idService, @RequestBody ServiceFacade serviceFacadeParam) {
        ServiceFacade serviceFacadeDb = serviceDataService.findById(idService);

        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (serviceFacadeParam.getAccountFacade().getIdAccount() != principal.getIdAccount()
                || serviceFacadeDb.getAccountFacade().getIdAccount() != principal.getIdAccount()) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "Authenticated user is not matches with account in updation service",
                    "PUT:/services/" + idService
            ), HttpStatus.FORBIDDEN);
        }

        serviceFacadeParam.setIdService(idService);

        return ResponseEntity.ok(serviceDataService.update(serviceFacadeParam));
    }

    /**
     * Удаляет из базы данных указанную услугу. Необходимо иметь роль мастера. Невозможно удалить чужую услугу.
     *
     * @param idService идентификатор услуги, которую необходимо удалить
     * @return json-ответ с кодом
     */
    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/services/{idService}")
    public ResponseEntity<Facade> deleteService(@PathVariable int idService) {
        ServiceFacade serviceFacade = serviceDataService.findById(idService);

        CustomPrincipal customPrincipal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (serviceFacade.getAccountFacade().getIdAccount() != customPrincipal.getIdAccount()) {

            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "You can't delete not your service",
                    "DELETE:/services/" + idService
            ), HttpStatus.FORBIDDEN);
        }

        serviceDataService.deleteById(idService);

        return ResponseEntity.ok(new CustomResponseObject(
                HttpStatus.OK.value(),
                "Service with id " + idService + " deleted",
                "DELETE:/services/" + idService
        ));
    }
}
