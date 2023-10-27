package net.chikaboom.facade.service;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.CommentFacadeConverter;
import net.chikaboom.facade.dto.CommentFacade;
import net.chikaboom.model.database.Comment;
import net.chikaboom.service.data.CommentDataService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Фасадный сервис-прослойка между контроллером и сервисом для работы с объектами избранного
 * Отражает, как действительно должно быть
 */
@Service
@RequiredArgsConstructor
public class CommentFacadeService {

    private final CommentDataService commentDataService;

    /**
     * Производит поиск комментария по его идентификатору. Конвертирует объект и возвращает
     * на контроллер
     *
     * @param idComment идентификатор искомого комментария
     * @return dto-объект комментария
     */
    public CommentFacade findById(int idComment) {
        Optional<Comment> commentOptional = commentDataService.findById(idComment);

        if (!commentOptional.isPresent()) {
            throw new NotFoundException("There not found comment with id " + idComment);
        }

        return CommentFacadeConverter.convertToDto(commentOptional.get());
    }

    /**
     * Производит поиск всех комментариев мастера, конвертирует в dto-объекты и отправляет на контроллер
     *
     * @param idAccountMaster идентификатор аккаунта мастера, чьи комментарии нужно найти
     * @return коллекцию комментариев, оставленных мастеру
     */
    public List<CommentFacade> findAllByIdAccountMaster(int idAccountMaster) {
        List<Comment> commentList = commentDataService.findByIdMaster(idAccountMaster);

        return commentList.stream().map(CommentFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Производит удаление комментария по его идентификатору
     *
     * @param idComment идентификатор комментария
     * @throws IllegalAccessException возникает, в случае попытки удаления чужого комментария
     */
    public void deleteById(int idComment) throws IllegalAccessException {
        commentDataService.deleteById(idComment);
    }

    /**
     * Создает новый объект комментария. Принимает dto-объект. Конвертирует в entity, сохраняет его в базу
     * и возвращает сконвертированный в dto созданный объект
     *
     * @param commentFacade создаваемый dto-объект комментария
     * @return созданный dto-объект комментария
     */
    public CommentFacade create(CommentFacade commentFacade) {
        Comment comment = CommentFacadeConverter.convertToModel(commentFacade);

        Comment createdComment = commentDataService.create(comment);
        //Нужно вытянуть данные, т.к. они нужны на клиенте. Мб сделать fetchType = eager
        createdComment.getAccountClient();
        createdComment.getAccountMaster();

        return CommentFacadeConverter.convertToDto(createdComment);
    }
}
