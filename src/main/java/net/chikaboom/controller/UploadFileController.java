package net.chikaboom.controller;

import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.service.action.UploadFileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер позволяет пользователю загружать файлы на сервер. Например фотографии
 */
@Controller
@RequestMapping("/chikaboom/upload/file/{idAccount}")
public class UploadFileController {

    private final UploadFileService uploadFileService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public UploadFileController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    /**
     * Получает данные файла с клиента и передает их на обработку на сервис
     *
     * @param idAccount идентификатор пользователя
     * @param fileName  наименование файла
     * @param file      сохраняемый файл
     * @return объект ответа с кодом 201, если файл загружен успешно, 400 - в ином случае
     */
    @PreAuthorize("hasAnyRole('MASTER', 'CLIENT') and #idAccount == authentication.principal.idAccount")
    @PostMapping
    public ResponseEntity<String> handleImageUpload(@PathVariable int idAccount,
                                                    @RequestParam String fileName,
                                                    @RequestParam MultipartFile file) {
        logger.info("Start to upload file");
        if (!file.isEmpty()) {

            uploadFileService.uploadFile(idAccount, fileName, file);

            return new ResponseEntity<>("File successfully uploaded", HttpStatus.CREATED);
        } else {
            throw new IncorrectInputDataException("File is empty. User has not chosen it");
        }
//        TODO если неверный формат данных
    }
}
