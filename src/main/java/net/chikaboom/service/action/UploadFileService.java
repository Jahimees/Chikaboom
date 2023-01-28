package net.chikaboom.service.action;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.service.ClientDataStorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UploadFileService implements ActionService {

    @Value("${attr.file}")
    private String FILE;
    @Value("${attr.fileName}")
    private String FILE_NAME;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${url.user.folder}")
    private String USER_FOLDER;

    private ClientDataStorageService clientDataStorageService;
    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public UploadFileService(ClientDataStorageService clientDataStorageService) {
        this.clientDataStorageService = clientDataStorageService;
    }

    /**
     * Сервис выполняет инициализацию папки пользователя, если такой нет и сохраняет в неё загруженный файл
     */
    @Override
    public void execute() {
        MultipartFile file = (MultipartFile) clientDataStorageService.getData(FILE);
        String fileName = clientDataStorageService.getData(FILE_NAME).toString();
        int idAccount = (int) clientDataStorageService.getData(ID_ACCOUNT);
        logger.info("Saving file data of user with id " + idAccount);

        File path = new File(USER_FOLDER + idAccount);
        if (!path.exists()) {
            logger.info("Creating new user folder in system");
            new File(USER_FOLDER + idAccount).mkdirs();
        }

        try (BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(
                        new File(USER_FOLDER + idAccount + "/" + fileName)))) {

            logger.info("Trying to save user file into system");
            byte[] bytes = file.getBytes();
            stream.write(bytes);

        } catch (FileNotFoundException ex) {
            logger.error(ex);
            throw new NoSuchDataException(ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
}
