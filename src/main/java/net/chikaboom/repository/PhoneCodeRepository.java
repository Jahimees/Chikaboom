package net.chikaboom.repository;

import net.chikaboom.model.database.PhoneCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы PhoneCode
 */
@Repository
public interface PhoneCodeRepository extends JpaRepository<PhoneCode, Integer> {

    /**
     * Находит сущность кода телефона по его сокращенному названию (название является уникальным в отличие от кода страны)
     * @param countryCut сокращенное название страны
     * @return сущность телефонного кода
     */
    PhoneCode findFirstByCountryCut(String countryCut);
}
