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

@Service
@RequiredArgsConstructor
public class CommentFacadeService {

    private final CommentDataService commentDataService;

    public CommentFacade findById(int idComment) {
        Optional<Comment> commentOptional = commentDataService.findById(idComment);

        if (!commentOptional.isPresent()) {
            throw new NotFoundException("There not found comment with id " + idComment);
        }

        return CommentFacadeConverter.convertToDto(commentOptional.get());
    }

    public List<CommentFacade> findAllByIdAccountMaster(int idAccountMaster) {
        List<Comment> commentList = commentDataService.findByIdMaster(idAccountMaster);

        return commentList.stream().map(CommentFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    public void deleteById(int idComment) throws IllegalAccessException {
        commentDataService.deleteById(idComment);
    }

    public CommentFacade patch(CommentFacade commentFacade) {
        Comment model = CommentFacadeConverter.convertToModel(commentFacade);

        return CommentFacadeConverter.convertToDto(commentDataService.patch(model));
    }

    public CommentFacade create(CommentFacade commentFacade) {
        Comment comment = CommentFacadeConverter.convertToModel(commentFacade);

        return CommentFacadeConverter.convertToDto(commentDataService.create(comment));
    }
}
