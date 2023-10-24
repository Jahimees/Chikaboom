package net.chikaboom.facade.service;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.facade.converter.FavoriteFacadeConverter;
import net.chikaboom.facade.dto.FavoriteFacade;
import net.chikaboom.model.database.Favorite;
import net.chikaboom.service.data.FavoriteDataService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteFacadeService {

    private final FavoriteDataService favoriteDataService;

    public FavoriteFacade findById(int idFavorite) {
        Optional<Favorite> favoriteOptional = favoriteDataService.findById(idFavorite);

        if (!favoriteOptional.isPresent()) {
            throw new NotFoundException("There not found favorite in database");
        }

        return FavoriteFacadeConverter.toAccountPage(favoriteOptional.get());
    }

    public FavoriteFacade findByIdFavoriteAndIdOwner(int idFavorite, int idFavoriteOwner) {
        Optional<Favorite> favoriteOptional = favoriteDataService.findByIdFavoriteAndIdOwner(idFavorite, idFavoriteOwner);

        if (!favoriteOptional.isPresent()) {
            throw new NotFoundException("There not found favorite in database");
        }

        return FavoriteFacadeConverter.toAccountPage(favoriteOptional.get());
    }

    public FavoriteFacade findByIdOwnerAndIdMaster(int idAccountOwner, int idFavoriteMaster) {
        Optional<Favorite> favoriteOptional = favoriteDataService.findByIdOwnerAndIdMaster
                (idAccountOwner, idFavoriteMaster);

        if (!favoriteOptional.isPresent()) {
            throw new NoSuchDataException("There not found favorite in database");
        }

        return FavoriteFacadeConverter.toAccountPage(favoriteOptional.get());
    }

    public List<FavoriteFacade> findAllByIdOwner(int idAccount) {
        List<Favorite> favoriteList = favoriteDataService.findAllByIdOwner(idAccount);

        return favoriteList.stream().map(FavoriteFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    public FavoriteFacade create(FavoriteFacade favoriteFacade) {
        Favorite createdFavorite = favoriteDataService.create(
                FavoriteFacadeConverter.convertToModel(favoriteFacade));

        return FavoriteFacadeConverter.toAccountPage(createdFavorite);
    }

    public void delete(FavoriteFacade favoriteFacade) {
        favoriteDataService.delete(FavoriteFacadeConverter.convertToModel(favoriteFacade));
    }

    public void deleteById(int idFavorite) {
        favoriteDataService.deleteById(idFavorite);
    }
}
