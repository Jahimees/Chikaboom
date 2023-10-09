package net.chikaboom.controller.rest.response;

import net.chikaboom.model.response.CustomResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для формирования объекта-ответа в зависимости от действия
 */
@RestController
public class SuccessResponseController {

    @GetMapping("/success/login")
    public ResponseEntity<CustomResponseObject> successLoginResponse() {
        return ResponseEntity.ok(new CustomResponseObject(200, "You successfully logged in", "/login"));
    }

    @GetMapping("/success/logout")
    public ResponseEntity<CustomResponseObject> successLogoutResponse() {
        return ResponseEntity.ok(new CustomResponseObject(200, "You successfully logged out", "/logout"));
    }
}
