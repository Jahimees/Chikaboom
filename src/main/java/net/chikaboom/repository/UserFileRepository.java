package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы UserFile
 */
@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Integer> {

    /**
     * Находит все файлы пользователя по аккаунту пользователя
     *
     * @param account аккаунт пользователя
     * @return список пользовательских файлов
     */
    List<UserFile> findAllByAccount(Account account);

    /**
     * Удаляет пользовательский файл по его пути и названию
     *
     * @param filePath путь и название пользовательского файла
     */
    void deleteByFilePath(String filePath);
}
