package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.PhoneCodeFacade;
import net.chikaboom.model.database.PhoneCode;

/**
 * DOCS {@link FacadeConverter}
 */
public final class PhoneCodeFacadeConverter implements FacadeConverter {

    private PhoneCodeFacadeConverter() {
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static PhoneCodeFacade convertToDto(PhoneCode model) {
        PhoneCodeFacade phoneCodeFacade = new PhoneCodeFacade();

        phoneCodeFacade.setIdPhoneCode(model.getIdPhoneCode());
        phoneCodeFacade.setPhoneCode(model.getPhoneCode());
        phoneCodeFacade.setCountryName(model.getCountryName());
        phoneCodeFacade.setCountryCut(model.getCountryCut());

        return phoneCodeFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static PhoneCode convertToModel(PhoneCodeFacade facade) {
        PhoneCode phoneCode = new PhoneCode();

        phoneCode.setIdPhoneCode(facade.getIdPhoneCode());
        phoneCode.setPhoneCode(facade.getPhoneCode());
        phoneCode.setCountryName(facade.getCountryName());
        phoneCode.setCountryCut(facade.getCountryCut());

        return phoneCode;
    }
}
