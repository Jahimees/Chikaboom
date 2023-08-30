package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.service.data.UserDetailsDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user-details")
@RequiredArgsConstructor
public class UserDetailsRestController {

    private final UserDetailsDataService userDetailsDataService;

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping
    public ResponseEntity<UserDetails> createUserDetails(@RequestBody UserDetails userDetails) {
        Account principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getIdUserDetails() != 0) {
            return ResponseEntity.badRequest().build();
        }

        if (userDetails.getMasterOwner() == null || userDetails.getMasterOwner().getIdAccount() != principal.getIdAccount()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(userDetailsDataService.create(userDetails));
    }
}
