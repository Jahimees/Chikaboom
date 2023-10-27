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

/**
 * REST контроллер для взаимодействия с сущностями типа {@link net.chikaboom.model.database.Comment}
 */
@RestController
@RequiredArgsConstructor
public class CommentRestController {

//    TODO REST DOCS
    private final CommentFacadeService commentFacadeService;

    /**
     * Возвращает все комментарии, оставленные мастеру
     * @param idAccount идентификатор аккаунта мастера
     * @return коллекцию комментариев
     */
    @GetMapping("/accounts/{idAccount}/comments")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CommentFacade>> findAllMastersComments(@PathVariable int idAccount) {
        return ResponseEntity.ok(commentFacadeService.findAllByIdAccountMaster(idAccount));
    }

    /**
     * Создает комментарий мастеру
     * @param idAccount идентификатор аккаунта мастера
     * @param commentFacade создаваемый комментарий
     * @return созданный комментарий
     */
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

    /**
     * Удаляет созданный пользователем комментарий. Комментарий может удалить только сам пользователь,
     * который написал его
     *
     * @param idAccount идентификатор аккаунта мастера, которому оставляется комментарий
     * @param idComment идентификатор удаляемого комментария
     * @return json-ответ об удалении комментария
     */
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
