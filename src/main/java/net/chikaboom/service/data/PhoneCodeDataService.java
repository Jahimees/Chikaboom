package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.PhoneCodeFacadeConverter;
import net.chikaboom.facade.dto.PhoneCodeFacade;
import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.repository.PhoneCodeRepository;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhoneCodeDataService {

    private final PhoneCodeRepository phoneCodeRepository;
    private final PhoneCodeFacadeConverter phoneCodeFacadeConverter;

    public PhoneCodeFacade findById(int id) {
        Optional<PhoneCode> phoneCodeOptional = phoneCodeRepository.findById(id);

        if (!phoneCodeOptional.isPresent()) {
            throw new NotFoundException("There not found phoneCode with id " + id);
        }

        return phoneCodeFacadeConverter.convertToDto(phoneCodeOptional.get());
    }

    public PhoneCodeFacade findFirstByCountryCut(String countryCut) {
        Optional<PhoneCode> phoneCodeOptional = phoneCodeRepository.findFirstByCountryCut(countryCut);

        if (!phoneCodeOptional.isPresent()) {
            throw new NotFoundException("There not found phoneCode with country cut " + countryCut);
        }

        return phoneCodeFacadeConverter.convertToDto(phoneCodeOptional.get());
    }
}
