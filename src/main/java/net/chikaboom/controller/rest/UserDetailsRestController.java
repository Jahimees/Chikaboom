package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.service.data.UserDetailsDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserDetailsRestController {

    private final UserDetailsDataService userDetailsDataService;
    private final AccountDataService accountDataService;

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping("/user-details")
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

    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @GetMapping("/accounts/{idAccount}/clients")
    public ResponseEntity<List<UserDetails>> findClients(@PathVariable int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userDetailsDataService.findClientsWithExtraInfo(idAccount));
    }
}
