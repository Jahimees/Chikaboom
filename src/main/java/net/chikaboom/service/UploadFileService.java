package net.chikaboom.service;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.facade.dto.UserFileFacade;
import net.chikaboom.facade.service.AccountFacadeService;
import net.chikaboom.facade.service.UserFileFacadeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * Сервис предназначен для загрузки и сохранения файлов на сервере
 */
@Service
@RequiredArgsConstructor
public class UploadFileService {

    @Value("${url.user.folder}")
    private String USER_FOLDER;
    private final UserFileFacadeService userFileFacadeService;
    private final AccountFacadeService accountFacadeService;

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Сервис выполняет инициализацию папки пользователя, если такой нет и сохраняет в неё загруженный файл
     */
    public UserFileFacade uploadFile(int idAccount, MultipartFile multipartFile, String fileName) {
        logger.info("Saving file data of user with id " + idAccount);
        UserFileFacade userFileFacade = new UserFileFacade();
        String newFileName;
        if (fileName != null && fileName.equals("avatar")) {
            newFileName = "avatar.jpeg";
        } else {
            newFileName = UUID.randomUUID() + ".jpeg";
        }


        File path = new File(USER_FOLDER + idAccount);
        if (!path.exists()) {
            logger.info("Creating new user folder in system");
            new File(USER_FOLDER + idAccount).mkdirs();
        }

        try (BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(
                        new File(USER_FOLDER + idAccount + "/" + newFileName)))) {

            if (fileName != null && fileName.equals("avatar")) {
                userFileFacadeService.deleteByFilePath(USER_FOLDER + idAccount + "/" + newFileName);
            }

            userFileFacade.setFilePath(USER_FOLDER + idAccount + "/" + newFileName);
            userFileFacade.setAccountFacade(accountFacadeService.findById(idAccount));
            userFileFacade = userFileFacadeService.create(userFileFacade);

            if (userFileFacade != null) {
                logger.info("Trying to save user file into system");
                byte[] bytes = multipartFile.getBytes();
                stream.write(bytes);
            }
        } catch (FileNotFoundException ex) {
            logger.error(ex);
            throw new NoSuchDataException(ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex);
        }
        return userFileFacade;
    }
}
