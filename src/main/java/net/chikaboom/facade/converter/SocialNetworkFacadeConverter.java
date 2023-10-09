package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.SocialNetworkFacade;
import net.chikaboom.model.database.SocialNetwork;
import org.springframework.stereotype.Component;

@Component
public class SocialNetworkFacadeConverter implements FacadeConverter<SocialNetworkFacade, SocialNetwork> {

    @Override
    public SocialNetworkFacade convertToDto(SocialNetwork model) {
        SocialNetworkFacade socialNetworkFacade = new SocialNetworkFacade();

        socialNetworkFacade.setIdSocialNetwork(model.getIdSocialNetwork());
        socialNetworkFacade.setLink(model.getLink());

        return socialNetworkFacade;
    }

    @Override
    public SocialNetwork convertToModel(SocialNetworkFacade facade) {
        SocialNetwork socialNetwork = new SocialNetwork();

        socialNetwork.setIdSocialNetwork(facade.getIdSocialNetwork());
        socialNetwork.setLink(facade.getLink());

        return socialNetwork;
    }
}
