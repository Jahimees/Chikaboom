package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.repository.PhoneCodeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис предоставляет возможность работы с сущностью кода телефона страны
 */
@Service
@RequiredArgsConstructor
public class PhoneCodeDataService {

    private final PhoneCodeRepository phoneCodeRepository;

    /**
     * Производит поиск кода телефона по идентификатору
     *
     * @param id идентификатор кода телефона
     * @return объект кода телефона
     */
    public Optional<PhoneCode> findById(int id) {
        return phoneCodeRepository.findById(id);
    }

    /**
     * Производит поиск по сокращенному названию страны
     *
     * @param countryCut сокращенное двухбуквенное название страны
     * @return объект кода телефона страны
     */
    public Optional<PhoneCode> findFirstByCountryCut(String countryCut) {
        return phoneCodeRepository.findFirstByCountryCut(countryCut);
    }
}
