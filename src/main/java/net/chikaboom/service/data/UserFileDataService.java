package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.UserFile;
import net.chikaboom.repository.UserFileRepository;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Сервис предоставляет возможность обработки данных пользовательских файлов.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserFileDataService implements DataService<UserFile> {

    private final UserFileRepository userFileRepository;

    /**
     * Производит поиск пользовательского файла по его идентификатору
     *
     * @param id идентификатор файла
     * @return пользовательский файл
     */
    @Override
    public Optional<UserFile> findById(int id) {
        return userFileRepository.findById(id);
    }

    /**
     * Производит поиск всех файлов в системе
     *
     * @return список найденных файлов
     * @deprecated этот метод, скорее всего, никогда не понадобится
     */
    @Override
    @Deprecated
    public List<UserFile> findAll() {
        return userFileRepository.findAll();
    }

    /**
     * Производит поиск всех файлов пользователя по его аккаунту
     *
     * @param account аккаунт пользователя, чьи файлы нужно найти
     * @return список найденных файлов
     */
    public List<UserFile> findAllByAccount(Account account) {
        return userFileRepository.findAllByAccount(account);
    }

    /**
     * Удаляет физический файл из системы, а также данные о нём из базы данных
     *
     * @param id идентифкатор файла в базе
     */
    @Override
    public void deleteById(int id) {
        Optional<UserFile> userFileOptional = findById(id);
        if (!userFileOptional.isPresent()) {
            throw new NotFoundException("There not found file with id " + id);
        }

        File file = new File(userFileOptional.get().getFilePath());
        boolean deleted = file.delete();
        if (deleted) {
            userFileRepository.deleteById(id);
        }
    }

    /**
     * Удаляет физический файл из системы и его данные из базы данных по пути и названию файла
     *
     * @param filePath путь и навзание файла
     */
    public void deleteByFilePath(String filePath) {
        boolean deleted = new File(filePath).delete();
        if (deleted) {
            userFileRepository.deleteByFilePath(filePath);
        }
    }

    /**
     * Производит обновление файла в базе данных
     *
     * @param userFile новый пользовательский файл
     * @return обновленный пользовательский файл
     */
    @Override
    public UserFile update(UserFile userFile) {
        return userFileRepository.saveAndFlush(userFile);
    }

    /**
     * создает новый пользовательский файл в системе
     *
     * @param userFile создаваемый пользовательский файл
     * @return созданный пользовательский файл
     */
    @Override
    public UserFile create(UserFile userFile) {
        return userFileRepository.saveAndFlush(userFile);
    }
}
