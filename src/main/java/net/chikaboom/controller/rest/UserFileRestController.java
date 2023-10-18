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

@RestController
@RequiredArgsConstructor
public class UserFileRestController {

    private final UserFileFacadeService userFileFacadeService;
    private final AccountFacadeService accountFacadeService;

    @PreAuthorize("permitAll()")
    @GetMapping("/accounts/{idAccount}/user_files")
    public ResponseEntity<List<UserFileFacade>> findAllUserFiles(@PathVariable int idAccount) {
        AccountFacade accountFacade = accountFacadeService.findById(idAccount);
        return ResponseEntity.ok(userFileFacadeService.findAllByAccount(accountFacade));
    }


    private final UploadFileService uploadFileService;
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Получает данные файла с клиента и передает их на обработку на сервис
     *
     * @param idAccount идентификатор пользователя
     * @param fileName  наименование файла
     * @param file      сохраняемый файл
     * @return объект ответа с кодом 201, если файл загружен успешно, 400 - в ином случае
     */
    @PreAuthorize("hasAnyRole('MASTER', 'CLIENT') and #idAccount == authentication.principal.idAccount")
    @PostMapping("/accounts/{idAccount}/user_files")
    public ResponseEntity<String> handleImageUpload(@PathVariable int idAccount,
                                                    @RequestParam String fileName,
                                                    @RequestParam MultipartFile file) {
        logger.info("Start to upload file");
        if (!file.isEmpty()) {

            uploadFileService.uploadFile(idAccount, fileName, file);

            return new ResponseEntity<>("File successfully uploaded", HttpStatus.CREATED);
        } else {
            throw new NoSuchElementException("File is empty. User has not chosen it");
        }
//        TODO если неверный формат данных
    }

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

//        TODO delete
        userFileFacadeService.deleteById(idUserFile);

        return ResponseEntity.ok(new CustomResponseObject(
                HttpStatus.OK.value(),
                "File deleted",
                "DELETE:/accounts/" + idAccount + "/user_files/" + idUserFile
                ));
    }
}
