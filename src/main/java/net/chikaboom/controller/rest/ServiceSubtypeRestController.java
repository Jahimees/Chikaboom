package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.service.data.ServiceSubtypeDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

//TODO проверки на совпадение id и PreAuthorize
@RequiredArgsConstructor
@RestController
public class ServiceSubtypeRestController {

    private final ServiceSubtypeDataService serviceSubtypeDataService;

    @GetMapping("/service-subtypes/{idServiceSubtype}")
    public ResponseEntity<ServiceSubtype> findServiceSubtype(int idServiceSubtype) {
        Optional<ServiceSubtype> serviceSubtype = serviceSubtypeDataService.findById(idServiceSubtype);

        if (!serviceSubtype.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(serviceSubtype.get());
    }

    @GetMapping("/service-subtypes")
    public ResponseEntity<List<ServiceSubtype>> findAllServices() {
        return ResponseEntity.ok(serviceSubtypeDataService.findAll());
    }
}
