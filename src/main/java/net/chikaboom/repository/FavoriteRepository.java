package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    List<Favorite> findAllByFavoriteOwner(Account favoriteOwner);

    Optional<Favorite> findByFavoriteOwnerAndFavoriteMaster(Account favoriteOwner, Account favoriteMaster);

    Optional<Favorite> findFavoriteByIdFavoriteAndFavoriteOwner(int idFavorite, Account favoriteOwner);

    void deleteByFavoriteOwnerAndFavoriteMaster(Account favoriteOwner, Account favoriteMaster);

    boolean existsByFavoriteOwnerAndFavoriteMaster(Account favoriteOwner, Account favoriteMaster);
}
