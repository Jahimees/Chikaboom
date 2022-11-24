package net.chikaboom.repository;

import net.chikaboom.model.database.PhoneCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы PhoneCode
 */
@Repository
public interface PhoneCodeRepository extends JpaRepository<PhoneCode, Integer> {

    PhoneCode findOneByPhoneCode(int phoneCode);
}
