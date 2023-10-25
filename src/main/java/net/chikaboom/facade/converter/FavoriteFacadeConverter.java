package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.FavoriteFacade;
import net.chikaboom.model.database.Favorite;

/**
 * DOCS {@link FacadeConverter}
 */
public final class FavoriteFacadeConverter implements FacadeConverter {

    private FavoriteFacadeConverter() {
    }

    /**
     * Конвертирует объект модели в DTO для отправки на страницу аккаунта
     *
     * @param model объект entity
     * @return конвертированный dto объект
     */
    public static FavoriteFacade toAccountPage(Favorite model) {
        FavoriteFacade facade = new FavoriteFacade();

        facade.setIdFavorite(model.getIdFavorite());
        facade.setFavoriteOwnerFacade(AccountFacadeConverter.toDtoOnlyId(model.getFavoriteOwner()));
        facade.setFavoriteMasterFacade(AccountFacadeConverter.toDtoOnlyId(model.getFavoriteMaster()));

        return facade;
    }

    public static FavoriteFacade toDtoForDataTable(Favorite model) {
        FavoriteFacade favoriteFacade = new FavoriteFacade();

        favoriteFacade.setIdFavorite(model.getIdFavorite());
        if (model.getFavoriteOwner() != null) {
            favoriteFacade.setFavoriteOwnerFacade(
                    AccountFacadeConverter.toDtoDataTable(model.getFavoriteOwner()));
        }
        if (model.getFavoriteMaster() != null) {
            favoriteFacade.setFavoriteMasterFacade(
                    AccountFacadeConverter.toDtoDataTable(model.getFavoriteMaster()));
        }

        return favoriteFacade;
    }

    /**
     * Полностью конвертирует объект entity в DTO
     *
     * @param model объект entity
     * @return конвертированный dto объект
     */
    public static FavoriteFacade convertToDto(Favorite model) {
        FavoriteFacade facade = new FavoriteFacade();

        facade.setIdFavorite(model.getIdFavorite());
        if (model.getFavoriteOwner() != null) {
            facade.setFavoriteOwnerFacade(AccountFacadeConverter.convertToDto(model.getFavoriteOwner()));
        }
        if (model.getFavoriteMaster() != null) {
            facade.setFavoriteMasterFacade(AccountFacadeConverter.convertToDto(model.getFavoriteMaster()));
        }

        return facade;
    }

    /**
     * Конвертирует DTO объект в Entity
     *
     * @param facade объект DTO
     * @return конвертированный entity объект
     */
    public static Favorite convertToModel(FavoriteFacade facade) {
        Favorite model = new Favorite();

        model.setIdFavorite(facade.getIdFavorite());
        if (facade.getFavoriteOwnerFacade() != null) {
            model.setFavoriteOwner(AccountFacadeConverter.convertToModel(facade.getFavoriteOwnerFacade()));
        }
        if (facade.getFavoriteMasterFacade() != null) {
            model.setFavoriteMaster(AccountFacadeConverter.convertToModel(facade.getFavoriteMasterFacade()));
        }

        return model;
    }
}
