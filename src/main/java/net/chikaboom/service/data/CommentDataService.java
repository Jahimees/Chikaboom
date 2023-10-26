package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Comment;
import net.chikaboom.model.database.CustomPrincipal;
import net.chikaboom.repository.CommentRepository;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentDataService {

    private final CommentRepository commentRepository;
    private final AccountDataService accountDataService;

    public Optional<Comment> findById(int id) {
        return commentRepository.findById(id);
    }

    public List<Comment> findByIdMaster(int idAccountMaster) {
        Optional<Account> accountOptional = accountDataService.findById(idAccountMaster);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("There not found account with id " + idAccountMaster);
        }

        return commentRepository.findAllByAccountMaster(accountOptional.get());
    }

    public void deleteById(int id) throws IllegalAccessException {
        Optional<Comment> commentOptional = findById(id);

        if (!commentOptional.isPresent()) {
            throw new NotFoundException("There not found comment with id " + id);
        }

        CustomPrincipal customPrincipal =
                (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (customPrincipal.getIdAccount() != commentOptional.get().getAccountClient().getIdAccount()) {
            throw new IllegalAccessException("You can't delete not your comments");
        }

        commentRepository.deleteById(id);
    }

    public Comment patch(Comment comment) {
        Optional<Comment> commentOptionalDb = findById(comment.getIdComment());

        if (!commentOptionalDb.isPresent()) {
            throw new NotFoundException("There not found comment object id database");
        }

        Comment commentDb = commentOptionalDb.get();
        commentDb.setGood(comment.isGood());
        commentDb.setText(comment.getText());

        return commentRepository.saveAndFlush(comment);
    }

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
