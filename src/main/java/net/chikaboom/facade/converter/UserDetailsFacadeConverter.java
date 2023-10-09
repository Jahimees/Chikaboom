package net.chikaboom.facade.converter;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.UserDetailsFacade;
import net.chikaboom.model.database.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class UserDetailsFacadeConverter implements FacadeConverter<UserDetailsFacade, UserDetails> {

    private final AccountFacadeConverter accountFacadeConverter;
    private final AboutFacadeConverter aboutFacadeConverter;
    private final SocialNetworkFacadeConverter socialNetworkFacadeConverter;
    private final PhoneCodeFacadeConverter phoneCodeFacadeConverter;

    @Override
    public UserDetailsFacade convertToDto(UserDetails model) {
        UserDetailsFacade userDetailsFacade = new UserDetailsFacade();

        userDetailsFacade.setIdUserDetails(model.getIdUserDetails());
        userDetailsFacade.setMasterOwnerFacade(accountFacadeConverter.convertToDto(model.getMasterOwner()));
        userDetailsFacade.setAboutFacade(aboutFacadeConverter.convertToDto(model.getAbout()));
        userDetailsFacade.setSocialNetworkFacade(socialNetworkFacadeConverter.convertToDto(model.getSocialNetwork()));
        userDetailsFacade.setPhoneCodeFacade(phoneCodeFacadeConverter.convertToDto(model.getPhoneCode()));
        userDetailsFacade.setPhone(model.getPhone());
        userDetailsFacade.setDisplayedPhone(model.getDisplayedPhone());
        userDetailsFacade.setFirstName(model.getFirstName());
        userDetailsFacade.setLastName(model.getLastName());
        userDetailsFacade.setLastVisitDate((Timestamp) model.getLastVisitDate().clone());
        userDetailsFacade.setVisitCount(model.getVisitCount());

        return userDetailsFacade;
    }

    @Override
    public UserDetails convertToModel(UserDetailsFacade facade) {
        UserDetails userDetails = new UserDetails();

        userDetails.setIdUserDetails(facade.getIdUserDetails());
        userDetails.setMasterOwner(accountFacadeConverter.convertToModel(facade.getMasterOwnerFacade()));
        userDetails.setAbout(aboutFacadeConverter.convertToModel(facade.getAboutFacade()));
        userDetails.setSocialNetwork(socialNetworkFacadeConverter.convertToModel(facade.getSocialNetworkFacade()));
        userDetails.setPhoneCode(phoneCodeFacadeConverter.convertToModel(facade.getPhoneCodeFacade()));
        userDetails.setPhone(facade.getPhone());
        userDetails.setDisplayedPhone(facade.getDisplayedPhone());
        userDetails.setFirstName(facade.getFirstName());
        userDetails.setLastName(facade.getLastName());
        userDetails.setLastVisitDate((Timestamp) facade.getLastVisitDate().clone());
        userDetails.setVisitCount(facade.getVisitCount());

        return userDetails;
    }
}
