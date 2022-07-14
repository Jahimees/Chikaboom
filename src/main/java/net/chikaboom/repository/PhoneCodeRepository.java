package net.chikaboom.repository;

import net.chikaboom.model.database.PhoneCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы PhoneCode
 */
@Repository
public interface PhoneCodeRepository extends CrudRepository<PhoneCode, Integer> {

    PhoneCode findOneByPhoneCode(int phoneCode);
}
