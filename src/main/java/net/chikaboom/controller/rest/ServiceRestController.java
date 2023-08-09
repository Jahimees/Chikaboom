package net.chikaboom.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Service;
import net.chikaboom.service.data.ServiceDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping()
public class ServiceRestController {

    private final ObjectMapper objectMapper;
    private final ServiceDataService serviceDataService;

    @GetMapping("/{idService}")
    public ResponseEntity<Service> findService(@PathVariable int idService) {
        Optional<Service> serviceOptional = serviceDataService.findById(idService);

        return serviceOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping
    public ResponseEntity<List<Service>> findAllServices() {
        return new ResponseEntity<>(serviceDataService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        return ResponseEntity.ok(serviceDataService.create(service));
    }

    @PutMapping("/{idService}")
    public ResponseEntity<Service> replaceService(@PathVariable int idService, @RequestBody Service service) {
        Optional<Service> serviceOptional = serviceDataService.findById(idService);

        if (!serviceOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(serviceDataService.update(service));
    }

    @PatchMapping(path = "/{idService}", consumes = "application/json-patch+json")
    public ResponseEntity<Service> changeService(@PathVariable int idService, @RequestBody JsonPatch jsonPatch) {
        try {
            Service service = serviceDataService.findById(idService)
                    .orElseThrow(() -> new NoSuchDataException("Service with id " + idService + " not found."));
            Service patchedService = applyPatchToService(jsonPatch, service);

            return ResponseEntity.ok(serviceDataService.update(patchedService));
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchDataException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idService}")
    public void deleteService(@PathVariable int idService) {
        serviceDataService.deleteById(idService);
    }

    private Service applyPatchToService(JsonPatch jsonPatch, Service targetService)
            throws JsonProcessingException, JsonPatchException {
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(targetService, JsonNode.class));
        return objectMapper.treeToValue(patched, Service.class);
    }
}
