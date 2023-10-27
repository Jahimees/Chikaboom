package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Comment;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.repository.CommentRepository;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис предоставляет возможность обработки данных комментариев, оставляемых пользователями
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentDataService {

    private final CommentRepository commentRepository;
    private final AccountDataService accountDataService;

    /**
     * Производит поиск по идентификатору комментария
     *
     * @param idComment идентификатор комментария
     * @return найденный объект комментария
     */
    public Optional<Comment> findById(int idComment) {
        return commentRepository.findById(idComment);
    }

    /**
     * Производит поиск комментариев, оставленных мастеру
     *
     * @param idAccountMaster идентификатор мастера
     * @return коллекция комментариев
     */
    public List<Comment> findByIdMaster(int idAccountMaster) {
        Optional<Account> accountOptional = accountDataService.findById(idAccountMaster);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("There not found account with id " + idAccountMaster);
        }

        return commentRepository.findAllByAccountMaster(accountOptional.get());
    }

    /**
     * Производит удаление комментария по его идентификатору
     *
     * @param idComment идентификатор комментария
     * @throws IllegalAccessException возникает при попытке удаления чужого комментария
     */
    public void deleteById(int idComment) throws IllegalAccessException {
        Optional<Comment> commentOptional = findById(idComment);

        if (!commentOptional.isPresent()) {
            throw new NotFoundException("There not found comment with id " + idComment);
        }

        CustomPrincipal customPrincipal =
                (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (customPrincipal.getIdAccount() != commentOptional.get().getAccountClient().getIdAccount()) {
            throw new IllegalAccessException("You can't delete not your comments");
        }

        commentRepository.deleteById(idComment);
    }

    /**
     * Создает комментарий в базе данных
     *
     * @param comment создаваемый объект комментария
     * @return созданный объект комментария
     */
    public Comment create(Comment comment) {
        if (comment.getAccountMaster() == null || comment.getAccountClient() == null || comment.getText() == null) {
            throw new IllegalArgumentException("There are not enough parameters in comment object");
        }

        if (commentRepository.existsByAccountClientAndAccountMaster(
                comment.getAccountClient(), comment.getAccountMaster())) {
            throw new AlreadyExistsException("You can't create comment to the same master");
        }

        comment.setDate(Timestamp.valueOf(LocalDateTime.now()));

        return commentRepository.saveAndFlush(comment);
    }
}
