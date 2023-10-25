package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для CRUD обработки таблицы Favorite
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    /**
     * Находит все сущности избранного по аккаунту владельца избранных
     *
     * @param favoriteOwner аккаунт-владелец избранных
     * @return коллекция избранных объектов
     */
    List<Favorite> findAllByFavoriteOwner(Account favoriteOwner);

    /**
     * Производит поиск по аккаунту-владельцу избранных, а также по аккаунту-субъекту.
     * Поскольку, невозможно, чтобы у одного человека был один и тот же человек дважды записан в избранное,
     * то вернется точно не более одного объекта
     *
     * @param favoriteOwner  аккаунт-владелец (субъект) избранного
     * @param favoriteMaster аккаунт, являющийся избранным (объект)
     * @return найденный объект избранного
     */
    Optional<Favorite> findByFavoriteOwnerAndFavoriteMaster(Account favoriteOwner, Account favoriteMaster);

    /**
     * Производит поиск избранного по идентификатору избранного и аккаунту владельцу.
     * Может показаться, что аккаунт-владелец излишний, однако, это сделано для того,
     * чтобы окончательно убедиться, что передаваемый параметр-владелец с клиента
     * действительно владеет избранным
     *
     * @param idFavorite    идентификатор объекта избранного
     * @param favoriteOwner аккаунт-владелец избранного
     * @return избранный объект
     */
    Optional<Favorite> findFavoriteByIdFavoriteAndFavoriteOwner(int idFavorite, Account favoriteOwner);

    /**
     * Производит удаление объекта избранного из базы данных по аккаунту-владельцу (субъекту),
     * а также по аккаунту-объекту
     *
     * @param favoriteOwner  аккаунт-владельца (субъект)
     * @param favoriteMaster аккаунт-объект
     */
    void deleteByFavoriteOwnerAndFavoriteMaster(Account favoriteOwner, Account favoriteMaster);

    /**
     * Проверяет существование избранного по аккаунту-владельцу и аккаунту-объекту
     *
     * @param favoriteOwner  аккаунт-владельца (субъект)
     * @param favoriteMaster аккаунт-объект
     * @return true - объект найден, false - в противном случае
     */
    boolean existsByFavoriteOwnerAndFavoriteMaster(Account favoriteOwner, Account favoriteMaster);
}
