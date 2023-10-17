package net.chikaboom.service;

import net.chikaboom.exception.NoSuchDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Сервис предназначен для загрузки и сохранения файлов на сервере
 */
@Service
@Transactional
public class UploadFileService {

    @Value("${url.user.folder}")
    private String USER_FOLDER;

    private final Logger logger = Logger.getLogger(this.getClass());

//    TODO FIXME NEW доработать. Нужно сохранять куда-то название файлов

    /**
     * Сервис выполняет инициализацию папки пользователя, если такой нет и сохраняет в неё загруженный файл
     */
    public void uploadFile(int idAccount, String fileName, MultipartFile multipartFile) {
        logger.info("Saving file data of user with id " + idAccount);

        File path = new File(USER_FOLDER + idAccount);
        if (!path.exists()) {
            logger.info("Creating new user folder in system");
            new File(USER_FOLDER + idAccount).mkdirs();
        }

        if (fileName.split("/").length > 1) {
            if (!new File(USER_FOLDER + idAccount + "/gallery").exists()) {
                new File(USER_FOLDER + idAccount + "/gallery").mkdirs();
            }
        }

        try (BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(
                        new File(USER_FOLDER + idAccount + "/" + fileName)))) {

            logger.info("Trying to save user file into system");
            byte[] bytes = multipartFile.getBytes();
            stream.write(bytes);
            //saveToDb

        } catch (FileNotFoundException ex) {
            logger.error(ex);
            throw new NoSuchDataException(ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
}
