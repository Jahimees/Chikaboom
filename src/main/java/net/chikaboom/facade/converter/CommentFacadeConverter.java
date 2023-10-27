package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.CommentFacade;
import net.chikaboom.model.database.Comment;

import java.sql.Timestamp;

/**
 * DOCS {@link FacadeConverter}
 */
public final class CommentFacadeConverter implements FacadeConverter {

    private CommentFacadeConverter() {
    }

    /**
     * Конвертирует entity объект в объект-dto. Некоторые поля объектов Account убираются в целях безопасности
     * и производительности
     *
     * @param model конвертируемый объект
     * @return сконвертированный объект
     */
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

    /**
     * Конвертирует dto-объект в объект entity
     * @param facade конвертируемый dto-объект
     * @return конвертированный entity-объект
     */
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
