package net.chikaboom.facade.dto;

import lombok.Data;

@Data
public class FavoriteFacade implements Facade {

    private int idFavorite;
    private AccountFacade favoriteOwnerFacade;
    private AccountFacade favoriteMasterFacade;
}
