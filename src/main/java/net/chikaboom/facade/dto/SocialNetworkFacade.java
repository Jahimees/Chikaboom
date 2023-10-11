package net.chikaboom.facade.dto;

import lombok.Data;

/**
 * DOCS {@link Facade}
 */
@Data
public class SocialNetworkFacade implements Facade {

    /**
     * id сущности в таблице social_network
     */
    private int idSocialNetwork;

    /**
     * Ссылка на социальную сеть
     */
    private String link;
}
