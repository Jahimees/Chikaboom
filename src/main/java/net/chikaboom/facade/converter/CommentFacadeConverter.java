package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.CommentFacade;
import net.chikaboom.model.database.Comment;

import java.sql.Timestamp;

public final class CommentFacadeConverter implements FacadeConverter {

    private CommentFacadeConverter() {
    }

    public static CommentFacade convertToDto(Comment model) {
        CommentFacade commentFacade = new CommentFacade();

        commentFacade.setIdComment(model.getIdComment());
        if (model.getAccountClient() != null) {
            commentFacade.setAccountClientFacade(AccountFacadeConverter.toDtoDataTable(model.getAccountClient()));
        }
        if (model.getAccountMaster() != null) {
            commentFacade.setAccountMasterFacade(AccountFacadeConverter.toDtoDataTable(model.getAccountMaster()));
        }
        if (model.getDate() != null) {
            commentFacade.setDate((Timestamp) model.getDate().clone());
        }
        commentFacade.setGood(model.isGood());
        commentFacade.setText(model.getText());

        return commentFacade;
    }

    public static Comment convertToModel(CommentFacade facade) {
        Comment comment = new Comment();

        comment.setIdComment(facade.getIdComment());
        if (facade.getAccountClientFacade() != null) {
            comment.setAccountClient(AccountFacadeConverter.convertToModel(facade.getAccountClientFacade()));
        }
        if (facade.getAccountMasterFacade() != null) {
            comment.setAccountMaster(AccountFacadeConverter.convertToModel(facade.getAccountMasterFacade()));
        }
        if (facade.getDate() != null) {
            comment.setDate((Timestamp) facade.getDate().clone());
        }
        comment.setGood(facade.isGood());
        comment.setText(facade.getText());

        return comment;
    }
}
