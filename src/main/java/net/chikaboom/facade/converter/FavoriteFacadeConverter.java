package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.FavoriteFacade;
import net.chikaboom.model.database.Favorite;

public final class FavoriteFacadeConverter implements FacadeConverter {

    private FavoriteFacadeConverter() {
    }

    public static FavoriteFacade toAccountPage(Favorite model) {
        FavoriteFacade facade = new FavoriteFacade();

        facade.setIdFavorite(model.getIdFavorite());
        facade.setFavoriteOwnerFacade(AccountFacadeConverter.toDtoOnlyId(model.getFavoriteOwner()));
        facade.setFavoriteMasterFacade(AccountFacadeConverter.toDtoOnlyId(model.getFavoriteMaster()));

        return facade;
    }

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
