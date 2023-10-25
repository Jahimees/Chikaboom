package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.Facade;
import net.chikaboom.facade.dto.FavoriteFacade;
import net.chikaboom.facade.service.FavoriteFacadeService;
import net.chikaboom.model.response.CustomResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для взаимодействия с сущностями типа {@link net.chikaboom.model.database.Favorite}
 */
//TODO docs
@RestController
@RequiredArgsConstructor
public class FavoriteRestController {

    private final FavoriteFacadeService favoriteFacadeService;

    /**
     * Производит поиск избранного по его идентификатору.
     * Для доступа, необходимо быть авторизованным и быть владельцем избранного объекта
     *
     * @param idAccount  идентификатор аккаунта
     * @param idFavorite идентификатор избранного
     * @return json-объект избранного
     */
    @GetMapping("/accounts/{idAccount}/favorites/{idFavorite}")
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    public ResponseEntity<FavoriteFacade> findFavoriteById(@PathVariable int idAccount, @PathVariable int idFavorite) {
        return ResponseEntity.ok(favoriteFacadeService.findByIdFavoriteAndIdOwner(idFavorite, idAccount));
    }

    /**
     * Производит поиск всех избранных объектов для определенного аккаунта
     *
     * @param idAccount идентификатор аккаунта
     * @return коллекцию со всеми объектами-избранными, которыми владеет указанный пользователь
     */
    @GetMapping("/accounts/{idAccount}/favorites")
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    public ResponseEntity<List<FavoriteFacade>> findAccountFavorites(@PathVariable int idAccount) {
        return ResponseEntity.ok(favoriteFacadeService.findAllByIdOwner(idAccount));
    }

    /**
     * Производит поиск избранного по идентификатору аккаунта-владельца (субъекта) и идентификатору
     * аккаунта-объекта
     *
     * @param idFavoriteMaster идентификатор аккаунта-субъекта
     * @param idFavoriteOwner  идентификатор аккаунта-объекта
     * @return найденный объект избранного
     */
    @GetMapping("/favorites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Facade> findFavoriteByMasterAndOwner(@RequestParam int idFavoriteMaster,
                                                               @RequestParam int idFavoriteOwner) {

        return ResponseEntity.ok(favoriteFacadeService.findByIdOwnerAndIdMaster(idFavoriteOwner, idFavoriteMaster));
    }

    /**
     * Производит создание объекта-избранного
     *
     * @param idAccount      идентификатор аккаунта-владельца
     * @param favoriteFacade создаваемый объект
     * @return созданный объект избранного
     */
    @PostMapping("/accounts/{idAccount}/favorites")
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    public ResponseEntity<Facade> addToFavorite(@PathVariable int idAccount,
                                                @RequestBody FavoriteFacade favoriteFacade) {
        if (favoriteFacade == null) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.BAD_REQUEST.value(),
                    "Got argument is null",
                    "POST:/accounts/" + idAccount + "/favorites"
            ), HttpStatus.BAD_REQUEST);
        }

        favoriteFacade.getFavoriteOwnerFacade().setIdAccount(idAccount);

        return ResponseEntity.ok(favoriteFacadeService.create(favoriteFacade));
    }

    /**
     * Производит удаление объекта избранного
     *
     * @param idAccount  идентификатор аккаунта-владельца (субъекта)
     * @param idFavorite идентификатор удаляемой сущности избранного
     * @return json-ответ об успешном/не успешном удалении объекта
     */
    @DeleteMapping("/accounts/{idAccount}/favorites/{idFavorite}")
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    public ResponseEntity<Facade> deleteFromFavorite(@PathVariable int idAccount,
                                                     @PathVariable int idFavorite) {
        FavoriteFacade favoriteFacade = favoriteFacadeService.findByIdFavoriteAndIdOwner(idFavorite, idAccount);
        favoriteFacadeService.delete(favoriteFacade);

        return ResponseEntity.ok(new CustomResponseObject(
                HttpStatus.OK.value(),
                "Favorite " + idFavorite + " deleted",
                "DELETE:/accounts/" + idAccount + "/favorites/" + idFavorite
        ));
    }
}
