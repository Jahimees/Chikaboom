package net.chikaboom.controller.rest;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.UserFileFacade;
import net.chikaboom.facade.service.AccountFacadeService;
import net.chikaboom.facade.service.UserFileFacadeService;
import net.chikaboom.model.response.CustomResponseObject;
import net.chikaboom.service.UploadFileService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST-контроллер для управления сущностью {@link net.chikaboom.model.database.UserFile}
 */
@RestController
@RequiredArgsConstructor
public class UserFileRestController {

    private final UserFileFacadeService userFileFacadeService;
    private final AccountFacadeService accountFacadeService;
    private final UploadFileService uploadFileService;
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Находит все файлы пользователя
     *
     * @param idAccount идентификатор пользователя
     * @return коллекцию файлов пользователя
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/accounts/{idAccount}/user_files")
    public ResponseEntity<List<UserFileFacade>> findAllUserFiles(@PathVariable int idAccount) {
        AccountFacade accountFacade = accountFacadeService.findById(idAccount);
        return ResponseEntity.ok(userFileFacadeService.findAllByAccount(accountFacade));
    }

    /**
     * Получает данные файла с клиента и передает их на обработку на сервис
     *
     * @param idAccount идентификатор пользователя
     * @param file      сохраняемый файл
     * @return объект ответа с кодом 201, если файл загружен успешно, 400 - в ином случае
     */
    @PreAuthorize("hasAnyRole('MASTER', 'CLIENT') and #idAccount == authentication.principal.idAccount")
    @PostMapping("/accounts/{idAccount}/user_files")
    public ResponseEntity<UserFileFacade> handleImageUpload(@PathVariable int idAccount,
                                                            @RequestParam MultipartFile file,
                                                            @RequestParam(required = false) String fileName) {
        logger.info("Start to upload file");
        if (!file.isEmpty()) {

            UserFileFacade userFileFacade = uploadFileService.uploadFile(idAccount, file, fileName);

            return new ResponseEntity<>(userFileFacade, HttpStatus.CREATED);
        } else {
            throw new NoSuchElementException("File is empty. User has not chosen it");
        }
    }

    /**
     * Удаляет пользовательский файл и из базы данных и из системы
     *
     * @param idAccount  идентификатор аккаунта
     * @param idUserFile идентификатор пользовательского файла
     * @return json-ответ
     */
    @PreAuthorize("isAuthenticated() && #idAccount == authentication.principal.idAccount")
    @DeleteMapping("/accounts/{idAccount}/user_files/{idUserFile}")
    public ResponseEntity<CustomResponseObject> deleteUserFile(@PathVariable int idAccount, @PathVariable int idUserFile) {
        AccountFacade accountFacade = accountFacadeService.findById(idAccount);
        UserFileFacade userFileFacade = userFileFacadeService.findById(idUserFile);

        if (accountFacade.getIdAccount() != userFileFacade.getAccountFacade().getIdAccount()) {
            return new ResponseEntity<>(new CustomResponseObject(
                    HttpStatus.FORBIDDEN.value(),
                    "You can't delte not your file",
                    "DELETE:/accounts/" + idAccount + "/user_files/" + idUserFile),
                    HttpStatus.FORBIDDEN
            );
        }

        userFileFacadeService.deleteById(idUserFile);

        return ResponseEntity.ok(new CustomResponseObject(
                HttpStatus.OK.value(),
                "File deleted",
                "DELETE:/accounts/" + idAccount + "/user_files/" + idUserFile
        ));
    }
}
