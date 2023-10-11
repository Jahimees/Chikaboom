package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.UserDetailsFacade;
import net.chikaboom.model.database.UserDetails;

import java.sql.Timestamp;

/**
 * DOCS {@link FacadeConverter}
 */
public final class UserDetailsFacadeConverter implements FacadeConverter {

    private UserDetailsFacadeConverter() {}

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static UserDetailsFacade convertToDto(UserDetails model) {
        UserDetailsFacade userDetailsFacade = new UserDetailsFacade();

        userDetailsFacade.setIdUserDetails(model.getIdUserDetails());
        userDetailsFacade.setPhone(model.getPhone());
        userDetailsFacade.setDisplayedPhone(model.getDisplayedPhone());
        userDetailsFacade.setFirstName(model.getFirstName());
        userDetailsFacade.setLastName(model.getLastName());
        userDetailsFacade.setVisitCount(model.getVisitCount());
        if (model.getLastVisitDate() != null) {
            userDetailsFacade.setLastVisitDate((Timestamp) model.getLastVisitDate().clone());
        }
        if (model.getMasterOwner() != null) {
            userDetailsFacade.setMasterOwnerFacade(AccountFacadeConverter.convertToDto(model.getMasterOwner()));
        }
        if (model.getAbout() != null) {
            userDetailsFacade.setAboutFacade(AboutFacadeConverter.convertToDto(model.getAbout()));
        }
        if (model.getSocialNetwork() != null) {
            userDetailsFacade.setSocialNetworkFacade(SocialNetworkFacadeConverter.convertToDto(model.getSocialNetwork()));
        }
        if (model.getPhoneCode() != null) {
            userDetailsFacade.setPhoneCodeFacade(PhoneCodeFacadeConverter.convertToDto(model.getPhoneCode()));
        }

        return userDetailsFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static UserDetails convertToModel(UserDetailsFacade facade) {
        UserDetails userDetails = new UserDetails();

        userDetails.setIdUserDetails(facade.getIdUserDetails());
        userDetails.setPhone(facade.getPhone());
        userDetails.setDisplayedPhone(facade.getDisplayedPhone());
        userDetails.setFirstName(facade.getFirstName());
        userDetails.setLastName(facade.getLastName());
        userDetails.setVisitCount(facade.getVisitCount());
        if (facade.getLastVisitDate() != null) {
            userDetails.setLastVisitDate((Timestamp) facade.getLastVisitDate().clone());
        }
        if (facade.getMasterOwnerFacade() != null) {
            userDetails.setMasterOwner(AccountFacadeConverter.convertToModel(facade.getMasterOwnerFacade()));
        }
        if (facade.getAboutFacade() != null) {
            userDetails.setAbout(AboutFacadeConverter.convertToModel(facade.getAboutFacade()));
        }
        if (facade.getSocialNetworkFacade() != null) {
            userDetails.setSocialNetwork(SocialNetworkFacadeConverter.convertToModel(facade.getSocialNetworkFacade()));
        }
        if (facade.getPhoneCodeFacade() != null) {
            userDetails.setPhoneCode(PhoneCodeFacadeConverter.convertToModel(facade.getPhoneCodeFacade()));
        }

        return userDetails;
    }
}
