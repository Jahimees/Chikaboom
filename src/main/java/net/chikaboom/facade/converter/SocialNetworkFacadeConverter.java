package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.SocialNetworkFacade;
import net.chikaboom.model.database.SocialNetwork;

/**
 * DOCS {@link FacadeConverter}
 */
public final class SocialNetworkFacadeConverter implements FacadeConverter {

    private SocialNetworkFacadeConverter() {
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static SocialNetworkFacade convertToDto(SocialNetwork model) {
        SocialNetworkFacade socialNetworkFacade = new SocialNetworkFacade();

        socialNetworkFacade.setIdSocialNetwork(model.getIdSocialNetwork());
        socialNetworkFacade.setLink(model.getLink());

        return socialNetworkFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static SocialNetwork convertToModel(SocialNetworkFacade facade) {
        SocialNetwork socialNetwork = new SocialNetwork();

        socialNetwork.setIdSocialNetwork(facade.getIdSocialNetwork());
        socialNetwork.setLink(facade.getLink());

        return socialNetwork;
    }
}
