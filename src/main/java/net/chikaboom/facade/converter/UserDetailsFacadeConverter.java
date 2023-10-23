package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.UserDetailsFacade;
import net.chikaboom.model.database.UserDetails;

import java.sql.Timestamp;

/**
 * DOCS {@link FacadeConverter}
 */
public final class UserDetailsFacadeConverter implements FacadeConverter {

    private UserDetailsFacadeConverter() {
    }

    /**
     * Для таблицы с записями на услуги
     */
    public static UserDetailsFacade toDtoAppointmentDataTable(UserDetails model) {
        UserDetailsFacade userDetailsFacade = new UserDetailsFacade();

        userDetailsFacade.setIdUserDetails(model.getIdUserDetails());
        userDetailsFacade.setDisplayedPhone(model.getDisplayedPhone());
        userDetailsFacade.setFirstName(model.getFirstName());
        userDetailsFacade.setLastName(model.getLastName());

        if (model.getMasterOwner() != null) {
            userDetailsFacade.setMasterOwnerFacade(AccountFacadeConverter.toDtoOnlyId(model.getMasterOwner()));
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
     * Конвертирует объект только для авторизованного пользователя
     */
    public static UserDetailsFacade toDtoForAccountUser(UserDetails model) {
        UserDetailsFacade userDetailsFacade = convertToDto(model);

        userDetailsFacade.setPhone(null);
        userDetailsFacade.setVisitCount(0);
        userDetailsFacade.setMasterOwnerFacade(null);
        userDetailsFacade.setLastVisitDate(null);
        userDetailsFacade.setFirstVisitDate(null);

        return userDetailsFacade;
    }

    /**
     * masterOwner - only id; about - null; socialNetwork - null;
     */
    public static UserDetailsFacade toDtoForClientsDataTable(UserDetails model) {
        UserDetailsFacade userDetailsFacade = new UserDetailsFacade();

        userDetailsFacade.setIdUserDetails(model.getIdUserDetails());
        userDetailsFacade.setDisplayedPhone(model.getDisplayedPhone());
        userDetailsFacade.setFirstName(model.getFirstName());
        userDetailsFacade.setLastName(model.getLastName());
        userDetailsFacade.setVisitCount(model.getVisitCount());
        if (model.getLastVisitDate() != null) {
            userDetailsFacade.setLastVisitDate((Timestamp) model.getLastVisitDate().clone());
        }
        if (model.getFirstVisitDate() != null) {
            userDetailsFacade.setFirstVisitDate((Timestamp) model.getFirstVisitDate().clone());
        }
        if (model.getMasterOwner() != null) {
            userDetailsFacade.setMasterOwnerFacade(AccountFacadeConverter.toDtoOnlyId(model.getMasterOwner()));
        }
        if (model.getPhoneCode() != null) {
            userDetailsFacade.setPhoneCodeFacade(PhoneCodeFacadeConverter.convertToDto(model.getPhoneCode()));
        }

        return userDetailsFacade;
    }

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
        if (model.getFirstVisitDate() != null) {
            userDetailsFacade.setFirstVisitDate((Timestamp) model.getFirstVisitDate().clone());
        }
//        TODO warning place 6 what case??
        if (model.getMasterOwner() != null) {
            userDetailsFacade.setMasterOwnerFacade(AccountFacadeConverter.toDtoForNotAccountUser(model.getMasterOwner()));
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
        if (facade.getFirstVisitDate() != null) {
            userDetails.setFirstVisitDate((Timestamp) facade.getFirstVisitDate().clone());
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
