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

@RestController
@RequiredArgsConstructor
public class FavoriteRestController {

    private final FavoriteFacadeService favoriteFacadeService;

    @GetMapping("/accounts/{idAccount}/favorites/{idFavorite}")
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    public ResponseEntity<FavoriteFacade> findFavoriteById(@PathVariable int idAccount, @PathVariable int idFavorite) {
        return ResponseEntity.ok(favoriteFacadeService.findByIdFavoriteAndIdOwner(idFavorite, idAccount));
    }

    @GetMapping("/accounts/{idAccount}/favorites")
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    public ResponseEntity<List<FavoriteFacade>> findAccountFavorites(@PathVariable int idAccount) {
        return ResponseEntity.ok(favoriteFacadeService.findAllByIdOwner(idAccount));
    }

    @GetMapping("/favorites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Facade> findFavoriteByMasterAndOwner(@RequestParam int idFavoriteMaster,
                                                               @RequestParam int idFavoriteOwner) {

        return ResponseEntity.ok(favoriteFacadeService.findByIdOwnerAndIdMaster(idFavoriteOwner, idFavoriteMaster));
    }

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

        return ResponseEntity.ok(favoriteFacadeService.create(favoriteFacade));
    }

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
