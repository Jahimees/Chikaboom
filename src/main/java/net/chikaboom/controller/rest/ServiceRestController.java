package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Service;
import net.chikaboom.service.data.ServiceDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ServiceRestController {

    private final ServiceDataService serviceDataService;

    @PreAuthorize("permitAll()")
    @GetMapping("/services/{idService}")
    public ResponseEntity<Service> findService(@PathVariable int idService) {
        Optional<Service> serviceOptional = serviceDataService.findById(idService);

        return serviceOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/services")
    public ResponseEntity<List<Service>> findAllServices() {
        return ResponseEntity.ok(serviceDataService.findAll());
    }

    /**
     * Создает данные об услуге мастера
     *
     * @param service услуга, которую предоставляет мастер
     * @return обновленная услуга
     */
    @PreAuthorize("hasRole('MASTER')")
    @PostMapping("/services")
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        Account masterAccount = service.getAccount();
        Account authorizedAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authorizedAccount.getIdAccount() != masterAccount.getIdAccount()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(serviceDataService.create(service));
    }

    /**
     * Обновляет данные об услуге мастера
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

        Account masterAccount = service.getAccount();
        Account authorizedAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authorizedAccount.getIdAccount() != masterAccount.getIdAccount()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        service.setIdService(idService);

        return ResponseEntity.ok(serviceDataService.update(service));
    }

    /**
     * Удаляет из базы данных указанную услугу
     *
     * @param idService идентификатор услуги, которую необходимо удалить
     * @return строку, содержащую результат удаления
     */
    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/services/{idService}")
    public ResponseEntity<Object> deleteService(@PathVariable int idService) {
        Optional<Service> serviceOptional = serviceDataService.findById(idService);

        if (!serviceOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account masterAccount = serviceOptional.get().getAccount();
        Account authorizedAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authorizedAccount.getIdAccount() != masterAccount.getIdAccount()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        serviceDataService.deleteById(idService);

        return ResponseEntity.ok().build();
    }

    /**
     * Загружает информацию обо всех услугах, который создал пользователь (idAccount - идентифицирует пользователя).
     *
     * @param idAccount идентификатор аккаунта
     * @return полную информацию в формате JSON обо всех созданных пользователем услугах.
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/accounts/{idAccount}/services")
    public List<Service> findAllServicesById(@PathVariable int idAccount) {
        return serviceDataService.findAllServicesByIdAccount(idAccount);
    }
}
