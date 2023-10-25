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

/**
 * Фасадный сервис-прослойка между контроллером и сервисом для работы с объектами избранного
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteFacadeService {

    private final FavoriteDataService favoriteDataService;

    /**
     * Производит поиск объекта избранного по идентификатору
     *
     * @param idFavorite идентификатор избранного объекта
     * @return найденный объект избранного
     */
    public FavoriteFacade findById(int idFavorite) {
        Optional<Favorite> favoriteOptional = favoriteDataService.findById(idFavorite);

        if (!favoriteOptional.isPresent()) {
            throw new NotFoundException("There not found favorite in database");
        }

        return FavoriteFacadeConverter.toAccountPage(favoriteOptional.get());
    }

    /**
     * Производит поиск объекта избранного по его идентификатору и идентификатору аккаунта-владельца (субъекта)
     *
     * @param idFavorite      идентификатор искомого объекта
     * @param idFavoriteOwner идентификатор аккаунта-субъекта
     * @return найденный объект избранного
     */
    public FavoriteFacade findByIdFavoriteAndIdOwner(int idFavorite, int idFavoriteOwner) {
        Optional<Favorite> favoriteOptional = favoriteDataService.findByIdFavoriteAndIdOwner(idFavorite, idFavoriteOwner);

        if (!favoriteOptional.isPresent()) {
            throw new NotFoundException("There not found favorite in database");
        }

        return FavoriteFacadeConverter.toAccountPage(favoriteOptional.get());
    }

    /**
     * Производит поиск по идентификатору аккаунта-владельца (субъекта) и идентификатору аккаунта-объекта
     *
     * @param idAccountOwner   идентификатор аккаунта-субъекта
     * @param idFavoriteMaster идентификатор аккаунта-объекта
     * @return найденный объект избранного
     */
    public FavoriteFacade findByIdOwnerAndIdMaster(int idAccountOwner, int idFavoriteMaster) {
        Optional<Favorite> favoriteOptional = favoriteDataService.findByIdOwnerAndIdMaster
                (idAccountOwner, idFavoriteMaster);

        if (!favoriteOptional.isPresent()) {
            throw new NoSuchDataException("There not found favorite in database");
        }

        return FavoriteFacadeConverter.toAccountPage(favoriteOptional.get());
    }

    /**
     * Производит поиск всех сущностей-избранных по идентификатору аккаунта владельца
     *
     * @param idAccount идентификатор аккаунта-субъекта
     * @return коллекцию объектов избранных, которыми владеет указанный аккаунт
     */
    public List<FavoriteFacade> findAllByIdOwner(int idAccount) {
        List<Favorite> favoriteList = favoriteDataService.findAllByIdOwner(idAccount);

        return favoriteList.stream().map(FavoriteFacadeConverter::toDtoForDataTable).collect(Collectors.toList());
    }

    /**
     * Создает объект избранного
     *
     * @param favoriteFacade создаваемый объект
     * @return созданный объект
     */
    public FavoriteFacade create(FavoriteFacade favoriteFacade) {
        Favorite createdFavorite = favoriteDataService.create(
                FavoriteFacadeConverter.convertToModel(favoriteFacade));

        return FavoriteFacadeConverter.toAccountPage(createdFavorite);
    }

    /**
     * Удаляет объект избранного из базы данных
     *
     * @param favoriteFacade удаляемый объект
     */
    public void delete(FavoriteFacade favoriteFacade) {
        favoriteDataService.delete(FavoriteFacadeConverter.convertToModel(favoriteFacade));
    }

    /**
     * Удаляет избранный объект по его идентификатору
     *
     * @param idFavorite идентификатор удаляемого объекта
     */
    public void deleteById(int idFavorite) {
        favoriteDataService.deleteById(idFavorite);
    }
}
