package net.chikaboom.facade.service;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.AccountFacadeConverter;
import net.chikaboom.facade.converter.UserFileFacadeConverter;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.UserFileFacade;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.UserFile;
import net.chikaboom.service.data.UserFileDataService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Фасадный сервис-прослойка между контроллером и сервисом для работы с объектами пользовательских файлов
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserFileFacadeService {

    private final UserFileDataService userFileDataService;

    /**
     * Поиск пользовательского файла по идентификатору файла
     *
     * @param id идентификатор файла
     * @return пользовательский файл
     */
    public UserFileFacade findById(int id) {
        Optional<UserFile> userFileOptional = userFileDataService.findById(id);

        if (!userFileOptional.isPresent()) {
            throw new NotFoundException("There not found file with id " + id);
        }

        return UserFileFacadeConverter.convertToDto(userFileOptional.get());
    }

    /**
     * Производит поиск всех файлов
     *
     * @return список пользовательских файлов
     * @deprecated Не думаю, что этот метод когда-нибудь понадобится
     */
    @Deprecated
    public List<UserFileFacade> findAll() {
        return userFileDataService.findAll()
                .stream().map(UserFileFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Производит поиск всех пользовательских файлов конкретного пользователя
     *
     * @param accountFacade аккаунт пользователя, чьи файлы нужно найти
     * @return список пользовательских файлов
     */
    public List<UserFileFacade> findAllByAccount(AccountFacade accountFacade) {
        Account accountModel = AccountFacadeConverter.convertToModel(accountFacade);

        return userFileDataService.findAllByAccount(accountModel)
                .stream().map(UserFileFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Производит удаление пользовательского файла по идентификатору файла
     *
     * @param id идентификатор файла
     */
    public void deleteById(int id) {
        userFileDataService.deleteById(id);
    }

    /**
     * Производит удаление пользовательского файла по его пути и имени (поскльку они являются уникальными,
     * то можно гарантировать, что удалится только один файл)
     *
     * @param filePath путь и имя файла
     */
    public void deleteByFilePath(String filePath) {
        userFileDataService.deleteByFilePath(filePath);
    }

    /**
     * Производит обновление файла в системе
     *
     * @param userFileFacade новый файл, который заменит старый
     * @return измененный пользовательский файл
     */
    public UserFileFacade update(UserFileFacade userFileFacade) {

        UserFile userFileModel = UserFileFacadeConverter.convertToModel(userFileFacade);

        return UserFileFacadeConverter.convertToDto(userFileDataService.update(userFileModel));
    }

    /**
     * Создает путь к файлу в базе данных и его физическое представление в системе
     *
     * @param userFileFacade загружаемый файл
     * @return созданный объект файла
     */
    public UserFileFacade create(UserFileFacade userFileFacade) {

        UserFile userFileModel = UserFileFacadeConverter.convertToModel(userFileFacade);

        return UserFileFacadeConverter.convertToDto(
                userFileDataService.create(userFileModel));
    }
}
