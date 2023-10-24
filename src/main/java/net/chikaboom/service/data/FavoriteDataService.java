package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Favorite;
import net.chikaboom.repository.FavoriteRepository;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteDataService {

    private final FavoriteRepository favoriteRepository;
    private final AccountDataService accountDataService;

    public Optional<Favorite> findById(int idFavorite) {
        return favoriteRepository.findById(idFavorite);
    }

    public Optional<Favorite> findByIdFavoriteAndIdOwner(int idFavorite, int idFavoriteOwner) {
        Optional<Account> ownerOptional = accountDataService.findById(idFavoriteOwner);

        if (!ownerOptional.isPresent()) {
            throw new NotFoundException("There not found owner accounts");
        }

        return favoriteRepository.findFavoriteByIdFavoriteAndFavoriteOwner(idFavorite, ownerOptional.get());
    }

    public Optional<Favorite> findByIdOwnerAndIdMaster(int idAccountOwner, int idAccountMaster) {
        Optional<Account> favoriteOwnerOptional = accountDataService.findById(idAccountOwner);
        Optional<Account> favoriteMasterOptional = accountDataService.findById(idAccountMaster);

        if (!favoriteOwnerOptional.isPresent()) {
            throw new NotFoundException("There not found favorite owner account");
        }

        if (!favoriteMasterOptional.isPresent()) {
            throw new NotFoundException("There not found favorite master account");
        }

        return favoriteRepository.findByFavoriteOwnerAndFavoriteMaster(
                favoriteOwnerOptional.get(), favoriteMasterOptional.get());
    }

    public List<Favorite> findAllByIdOwner(int idAccount) {
        Optional<Account> favoriteOwnerOptional = accountDataService.findById(idAccount);

        if (!favoriteOwnerOptional.isPresent()) {
            throw new NotFoundException("There not found account with id " + idAccount);
        }

        return favoriteRepository.findAllByFavoriteOwner(favoriteOwnerOptional.get());
    }

    public Favorite create(Favorite favorite) {
        if (favorite == null) {
            throw new IllegalArgumentException("It is null favorite object value");
        }

        if (isFavoriteExists(favorite)) {
            throw new AlreadyExistsException("The favorite object already exists");
        }

        return favoriteRepository.saveAndFlush(favorite);
    }

    public void delete(Favorite favorite) {
        if (favorite.getIdFavorite() != 0) {
            favoriteRepository.deleteById(favorite.getIdFavorite());
        } else {
            if (favorite.getFavoriteMaster() != null && favorite.getFavoriteOwner() != null) {
                Optional<Favorite> favoriteOptional = findByIdOwnerAndIdMaster
                        (favorite.getFavoriteOwner().getIdAccount(), favorite.getFavoriteMaster().getIdAccount());

                if (!favoriteOptional.isPresent()) {
                    throw new NotFoundException("There not found favorite object in database");
                }

                favoriteRepository.deleteByFavoriteOwnerAndFavoriteMaster(favoriteOptional.get().getFavoriteOwner(),
                        favoriteOptional.get().getFavoriteMaster());
            } else {
                throw new NotFoundException("There not found favorite object in database");
            }
        }
    }

    public void deleteById(int idFavorite) {
        favoriteRepository.deleteById(idFavorite);
    }

    private boolean isFavoriteExists(Favorite favorite) {
        if (favorite.getFavoriteMaster() == null || favorite.getFavoriteOwner() == null) {
            throw new IllegalArgumentException("There are null values in favorite object");
        }

        Optional<Account> favoriteOwnerOptional = accountDataService.findById(favorite.getFavoriteOwner().getIdAccount());
        Optional<Account> favoriteMasterOptional = accountDataService.findById(favorite.getFavoriteMaster().getIdAccount());

        if (!favoriteOwnerOptional.isPresent()) {
            throw new NotFoundException("There not found favorite owner account");
        }

        if (!favoriteMasterOptional.isPresent()) {
            throw new NotFoundException("There not found favorite master account");
        }

        return favoriteRepository.existsByFavoriteOwnerAndFavoriteMaster
                (favoriteOwnerOptional.get(), favoriteMasterOptional.get());
    }
}
