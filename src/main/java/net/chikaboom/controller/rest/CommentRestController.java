package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.CommentFacade;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.facade.service.CommentFacadeService;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.model.response.CustomResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentFacadeService commentFacadeService;

    @GetMapping("/accounts/{idAccount}/comments")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CommentFacade>> findAllMastersComments(@PathVariable int idAccount) {
        return ResponseEntity.ok(commentFacadeService.findAllByIdAccountMaster(idAccount));
    }

    @PostMapping("/accounts/{idAccount}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Facade> createComment(@PathVariable int idAccount,
                                                @RequestBody CommentFacade commentFacade) {
        AccountFacade clientAccount = commentFacade.getAccountClientFacade();
        CustomPrincipal customPrincipal =
                (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (clientAccount.getIdAccount() != customPrincipal.getIdAccount()
        || clientAccount.getIdAccount() == idAccount) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "You can't create comment under different account",
                    "POST:/accounts/" + idAccount + "/comments"
            ), HttpStatus.FORBIDDEN);
        }

        commentFacade.getAccountMasterFacade().setIdAccount(idAccount);

        return ResponseEntity.ok(commentFacadeService.create(commentFacade));
    }

    @PatchMapping("/accounts/{idAccount}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Facade> patchComment(@PathVariable int idAccount, @RequestBody CommentFacade commentFacade) {
        CustomPrincipal customPrincipal =
                (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (commentFacade == null || commentFacade.getAccountClientFacade() == null ||
                commentFacade.getAccountClientFacade().getIdAccount() != customPrincipal.getIdAccount()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "You can't change not yours comment",
                    "PATCH:/accounts/" + idAccount + "/comments"
            ), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(commentFacadeService.patch(commentFacade));
    }

    @DeleteMapping("/accounts/{idAccount}/comments/{idComment}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Facade> deleteComment(@PathVariable int idAccount, @PathVariable int idComment) {
        try {
            commentFacadeService.deleteById(idComment);
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    e.getMessage(),
                    "DELETE:/accounts/" + idAccount + "/comments/" + idComment),
                    HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(new CustomResponseObject(
                HttpStatus.OK.value(),
                "Comment deleted",
                "DELETE:/accounts/" + idAccount + "/comments/" + idComment
        ));
    }
}
