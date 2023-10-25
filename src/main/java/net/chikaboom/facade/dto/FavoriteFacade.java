package net.chikaboom.facade.dto;

import lombok.Data;

/**
 * DOCS {@link Facade}
 */
@Data
public class FavoriteFacade implements Facade {

    /**
     * Идентификатор избранной сущности
     */
    private int idFavorite;

    /**
     * Аккаунт (субъект), который владеет избранной сущностью
     */
    private AccountFacade favoriteOwnerFacade;

    /**
     * Аккаунт (объект), кто является объектом избранной сущности
     */
    private AccountFacade favoriteMasterFacade;
}
