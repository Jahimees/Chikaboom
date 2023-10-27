package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Favorite;
import net.chikaboom.repository.FavoriteRepository;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис предоставляет возможность работы с сущностью избранного
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteDataService {

    private final FavoriteRepository favoriteRepository;
    private final AccountDataService accountDataService;

    /**
     * Производит поиск избранного по идентифкатору
     *
     * @param idFavorite идентификатор избранного
     * @return найденный объект
     */
    public Optional<Favorite> findById(int idFavorite) {
        return favoriteRepository.findById(idFavorite);
    }

    /**
     * Производит поиск избранного по идентификатору избранного и идентификатору владельца аккаунта.
     * Идентификатор аккаунта может показаться избыточным, однако, это сделано для того,
     * чтобы однозначно убедиться, что передаваемый параметр-владелец с клиента действительно
     * владее объектом избранного
     *
     * @param idFavorite      идентификатор избранного
     * @param idFavoriteOwner идентификатор аккаунта-владельца
     * @return объект избранного
     */
    public Optional<Favorite> findByIdFavoriteAndIdOwner(int idFavorite, int idFavoriteOwner) {
        Optional<Account> ownerOptional = accountDataService.findById(idFavoriteOwner);

        if (!ownerOptional.isPresent()) {
            throw new NotFoundException("There not found owner accounts");
        }

        return favoriteRepository.findFavoriteByIdFavoriteAndFavoriteOwner(idFavorite, ownerOptional.get());
    }

    /**
     * Производит поиск объекта избранного по идентификатору аккаунта-владельца (субъекта) и
     * по идентификатору аккаунта-объекта
     *
     * @param idAccountOwner  идентификатор аккаунта-субъекта
     * @param idAccountMaster идентификатор аккаунта-объекта
     * @return найденный объект избранного
     */
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

    /**
     * Производит поиск всех объектов избранного по идентификатору аккаунта-владельца (субъекта)
     *
     * @param idAccount идентификатор аккаунта-владельца (субъекта)
     * @return коллекцию избранного
     */
    public List<Favorite> findAllByIdOwner(int idAccount) {
        Optional<Account> favoriteOwnerOptional = accountDataService.findById(idAccount);

        if (!favoriteOwnerOptional.isPresent()) {
            throw new NotFoundException("There not found account with id " + idAccount);
        }

        return favoriteRepository.findAllByFavoriteOwner(favoriteOwnerOptional.get());
    }

    /**
     * Создание нового объекта-избранного
     *
     * @param favorite создаваемый объект
     * @return созданный объект
     */
    public Favorite create(Favorite favorite) {
        if (favorite == null) {
            throw new IllegalArgumentException("It is null favorite object value");
        }

        if (isFavoriteExists(favorite)) {
            throw new AlreadyExistsException("The favorite object already exists");
        }

        return favoriteRepository.saveAndFlush(favorite);
    }

    /**
     * Производит удаление избранного объекта
     *
     * @param favorite удаляемый объект
     */
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

    /**
     * Производит удаление избранного объекта по его идентификатору
     *
     * @param idFavorite идентификатор удаляемого объекта
     */
    public void deleteById(int idFavorite) {
        favoriteRepository.deleteById(idFavorite);
    }

    /**
     * Проверяет, существует ли объект избранного.
     * Проверка производится по существованию аккаунтов (субъекта и объекта),
     * а также по существовании записи избранного в базе с этими аккаунтами
     *
     * @param favorite проверяемый объект
     * @return true - объект существует, false - иначе
     */
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
