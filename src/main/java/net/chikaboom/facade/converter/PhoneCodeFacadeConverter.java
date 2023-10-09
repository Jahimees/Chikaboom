package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.PhoneCodeFacade;
import net.chikaboom.model.database.PhoneCode;
import org.springframework.stereotype.Component;

@Component
public class PhoneCodeFacadeConverter implements FacadeConverter<PhoneCodeFacade, PhoneCode> {

    @Override
    public PhoneCodeFacade convertToDto(PhoneCode model) {
        PhoneCodeFacade phoneCodeFacade = new PhoneCodeFacade();

        phoneCodeFacade.setIdPhoneCode(model.getIdPhoneCode());
        phoneCodeFacade.setPhoneCode(model.getPhoneCode());
        phoneCodeFacade.setCountryName(model.getCountryName());
        phoneCodeFacade.setCountryCut(model.getCountryCut());

        return phoneCodeFacade;
    }

    @Override
    public PhoneCode convertToModel(PhoneCodeFacade facade) {
        PhoneCode phoneCode = new PhoneCode();

        phoneCode.setIdPhoneCode(facade.getIdPhoneCode());
        phoneCode.setPhoneCode(facade.getPhoneCode());
        phoneCode.setCountryName(facade.getCountryName());
        phoneCode.setCountryCut(facade.getCountryCut());

        return phoneCode;
    }
}
